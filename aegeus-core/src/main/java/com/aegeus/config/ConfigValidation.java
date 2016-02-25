/*
 * Copyright 2015 Tarık Yılmaz
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
package com.aegeus.config;

import com.aegeus.config.format.ConfigObject;
import com.aegeus.config.format.DbConfigObject;
import com.aegeus.config.format.EmrConfigObject;
import com.aegeus.config.format.S3ConfigObject;
import com.aegeus.config.format.WorkflowConfigObject;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;

/**
 * This class will make validation over the config.yml data
 */
public class ConfigValidation
{
    private static final Logger logger = Logger.getLogger(ConfigValidation.class);

    private final ConfigObject config;

    public ConfigValidation(ConfigObject config) {
        this.config = config;
    }

    /**
     * Workflow object validation
     *
     * @throws IllegalValueException
     */
    private void validateWorkflow() throws IllegalValueException {
        WorkflowConfigObject workflow = config.getWorkflow();

        if (workflow.getLatency() < 1 || workflow.getLatency() > 1440) {
            throw new IllegalValueException("workflow.latency must be between 1 (1 min) and 1440 (1 day)");
        }

        if (workflow.getDbPort() < 1 || workflow.getDbPort() > 65535) {
            throw new IllegalValueException("workflow.dbPort must be between 1 and 65535");
        }

        if (workflow.getHttpPort() < 1 || workflow.getHttpPort() > 65535) {
            throw new IllegalValueException("workflow.httpPort must be between 1 and 65535");
        }

        if (workflow.getQueueSize() < 10 || workflow.getQueueSize() > 120) {
            throw new IllegalValueException("workflow.queueSize must be between 10 and 120");
        }

        if (workflow.getThreadSize() < 2 || workflow.getThreadSize() > 120) {
            throw new IllegalValueException(String.format("workflow.threadSize must be between 2 and 120, you have %d processor",
                    Runtime.getRuntime().availableProcessors()));
        }
    }

    /**
     * DB object validation
     *
     * @throws IllegalValueException
     */
    private void validateDb() throws IllegalValueException {
        DbConfigObject db = config.getDb();

        if (Strings.isNullOrEmpty(db.getHost())) {
            db.setHost("localhost");
        }

        if (db.getPort() < 1 || db.getPort() > 65535) {
            throw new IllegalValueException("db.port must be between 1 and 65535");
        }

    }

    /**
     * AWS properties validation
     *
     * @throws IllegalValueException
     * @throws MissingValueException
     */
    private void validateAws() throws IllegalValueException, MissingValueException {
        EmrConfigObject emr = config.getAws().getEmr();
        S3ConfigObject s3 = config.getAws().getS3();

        if (emr == null) {
            throw new MissingValueException("aws.emr object must be entered");
        }

        if (s3 == null) {
            throw new MissingValueException("aws.s3 object must be entered");
        }

        if (Strings.isNullOrEmpty(emr.getAccessKey())) {
            throw new MissingValueException("aws.emr.accessKey must be entered. Please check aws console AIM section. "
                    + "For information please check : "
                    + "http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSGettingStartedGuide/AWSCredentials.html");
        }

        if (Strings.isNullOrEmpty(emr.getSecretKey())) {
            throw new MissingValueException("aws.emr.secretKey must be entered. Please check aws console AIM section. "
                    + "For information please check : "
                    + "http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSGettingStartedGuide/AWSCredentials.html");
        }

        if (Strings.isNullOrEmpty(s3.getAccessKey())) {
            throw new MissingValueException("aws.s3.accessKey must be entered. Please check aws console AIM section. "
                    + "For information please check : "
                    + "http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSGettingStartedGuide/AWSCredentials.html");
        }

        if (Strings.isNullOrEmpty(s3.getSecretKey())) {
            throw new MissingValueException("aws.s3.secretKey must be entered. Please check aws console AIM section. "
                    + "For information please check : "
                    + "http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSGettingStartedGuide/AWSCredentials.html");
        }

        if (Strings.isNullOrEmpty(emr.getName())) {
            logger.info("aws.emr.name is empty. Cluster name assigned to `aegeus-`");
        }

        if (emr.getInstanceCount() < 2) {
            logger.info("aws.emr.instanceCount lower than 2. Instance count selected as 2");

            emr.setInstanceCount((byte) 2);
        }

        if (Strings.isNullOrEmpty(emr.getRegion())) {
            emr.setPlace("us-east-1");
        }

        if (Strings.isNullOrEmpty(emr.getMasterType())) {
            logger.info("aws.emr.masterType is empty. Master type selected as m3.xlarge");

            emr.setMasterType("m3x.large");
        }

        if (Strings.isNullOrEmpty(emr.getSlaveType())) {
            logger.info("aws.emr.slaveType is empty. Slave type selected as m3.xlarge");

            emr.setMasterType("m3x.large");
        }
    }

    /**
     * Updates {@link ConfigObject} with generic validators
     *
     * @return Validated and update configuration object
     * @throws IllegalValueException
     */
    public ConfigObject validate() throws IllegalValueException, MissingValueException {
        validateWorkflow();
        validateDb();
        validateAws();

        return config;
    }
}