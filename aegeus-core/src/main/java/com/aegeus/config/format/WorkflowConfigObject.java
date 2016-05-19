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

public class WorkflowConfigObject {
    private int latency;

    private int httpPort;

    private DbConfigObject metaStore;

    private byte queueSize;

    private byte threadSize;

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public DbConfigObject getMetaStore() {
        return metaStore;
    }

    public void setMetaStore(DbConfigObject metaStore) {
        this.metaStore = metaStore;
    }

    public byte getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(byte queueSize) {
        this.queueSize = queueSize;
    }

    public byte getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(byte threadSize) {
        this.threadSize = threadSize;
    }
}