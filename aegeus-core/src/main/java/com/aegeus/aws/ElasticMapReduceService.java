/*
 * Copyright 2016 Tarık Yılmaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aegeus.aws;

import com.aegeus.config.format.EmrConfigObject;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.*;

import com.google.common.base.Strings;

import org.apache.log4j.Logger;

import java.util.List;

public class ElasticMapReduceService {
    private static final Logger LOGGER = Logger.getLogger(ElasticMapReduceService.class);

    private static final int SLEEP = 10000;

    private EmrConfigObject config;

    private final AmazonElasticMapReduceClient emr;

    private String clusterId;

    private List<String> stepIds;

    public ElasticMapReduceService(EmrConfigObject config) {
        this.config = config;

        emr = new AmazonElasticMapReduceClient(new BasicAWSCredentials(
                config.getAccessKey(),
                config.getSecretKey()));
        emr.setRegion(Region.getRegion(Regions.fromName(config.getRegion())));
    }

    /**
     * Create a new EMR Cluster over Hadoop 2.4.0
     */
    public void createCluster() {
        JobFlowInstancesConfig instances = new JobFlowInstancesConfig()
                .withInstanceCount((int) config.getInstanceCount())
                .withMasterInstanceType(config.getMasterType())
                .withSlaveInstanceType(config.getSlaveType());

        if (Strings.isNullOrEmpty(config.getKeyName())) {
            instances.setEc2KeyName(config.getKeyName());
        }

        if (!Strings.isNullOrEmpty(config.getSubnetId())) {
            instances.setEc2SubnetId(config.getSubnetId());
        } else {
            instances.setPlacement(new PlacementType(config.getPlace()));
        }

        ScriptBootstrapActionConfig installEsConfig = new ScriptBootstrapActionConfig()
                .withPath("s3://support.elasticmapreduce/bootstrap-actions/other/elasticsearch_install.rb");

        BootstrapActionConfig installEs = new BootstrapActionConfig("Elasticsearch Install", installEsConfig);

        RunJobFlowRequest request = new RunJobFlowRequest()
                .withName(config.getName())
                .withReleaseLabel("emr-4.1.0")
                .withServiceRole("Default_AWS_Role")
                .withJobFlowRole("Default_AWS_Role")
                .withBootstrapActions(installEs)
                .withInstances(instances);

        if (!Strings.isNullOrEmpty(config.getLogBucket())) {
            request.setLogUri(config.getLogBucket());
        }

        RunJobFlowResult result = emr.runJobFlow(request);

        clusterId = result.getJobFlowId();
    }

    /**
     * Get current cluster id
     *
     * @return Cluster Id
     */
    public String getClusterId() {
        return clusterId;
    }

    /**
     * Wait cluster until cluster is ready
     */
    public void waitCluster() {
        String lastReason = null;
        DescribeClusterRequest request = new DescribeClusterRequest().withClusterId(clusterId);
        DescribeClusterResult result;

        while (true) {
            try {
                result = emr.describeCluster(request);
                String reason = result.getCluster().getStatus().getStateChangeReason().getMessage();

                if (!reason.equals(lastReason)) {
                    LOGGER.info("Cluster status changed. New status is " + reason);
                    lastReason = reason;
                }

                ClusterState status = ClusterState.fromValue(result.getCluster().getStatus().getState());
                if (status.equals(ClusterState.WAITING)) {
                    return;
                }

                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void addSteps() {
        StepConfig parseStep = new StepConfig()
                .withName("Parse logs")
                .withActionOnFailure(ActionOnFailure.TERMINATE_JOB_FLOW);

        StepConfig persistStep = new StepConfig()
                .withName("Persist layer")
                .withActionOnFailure(ActionOnFailure.TERMINATE_JOB_FLOW);

        AddJobFlowStepsRequest request = new AddJobFlowStepsRequest()
                .withJobFlowId(clusterId)
                .withSteps(parseStep, persistStep);

        stepIds = emr.addJobFlowSteps(request).getStepIds();
    }

    public void waitSteps() {

    }

    public void terminate() {
        if (clusterId != null) {
            TerminateJobFlowsRequest request = new TerminateJobFlowsRequest()
                    .withJobFlowIds(clusterId);

            emr.terminateJobFlows(request);
        }
    }

    public void close() {
        terminate();

        emr.shutdown();
    }
}