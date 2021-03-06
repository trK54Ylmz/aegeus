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
package com.aegeus.config.format;

import com.aegeus.config.Printable;

public class ConfigObject extends Printable {
    private WorkflowConfigObject workflow;

    private DbConfigObject db;

    private AwsConfigObject aws;

    public WorkflowConfigObject getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowConfigObject workflow) {
        this.workflow = workflow;
    }

    public DbConfigObject getDb() {
        return db;
    }

    public void setDb(DbConfigObject db) {
        this.db = db;
    }

    public AwsConfigObject getAws() {
        return aws;
    }

    public void setAws(AwsConfigObject aws) {
        this.aws = aws;
    }
}