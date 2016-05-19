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
package com.aegeus.app;

import com.aegeus.executor.ThreadPool;
import com.aegeus.config.format.ConfigObject;

import org.apache.log4j.Logger;

public class AppWrapper {
    private static final Logger logger = Logger.getLogger(AppWrapper.class);

    private final ConfigObject config;

    private final ThreadPool pool = new ThreadPool();

    public AppWrapper(ConfigObject config) {
        this.config = config;

        /**
         * Set current number of processor if thread size config is 0
         */
        if (config.getWorkflow().getThreadSize() == 0) {
            // Detect number of cores size
            byte cores = (byte) Runtime.getRuntime().availableProcessors();

            config.getWorkflow().setThreadSize(cores);
        }

        pool.setQueueSize(config.getWorkflow().getQueueSize());
        pool.setCoreSize(config.getWorkflow().getThreadSize());
        pool.createThreadPool();
    }

    public void start() {
        long SLEEP = config.getWorkflow().getLatency() * 60000;

        while (true) {
            try {
                pool.execute(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                Thread.sleep(SLEEP);
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
    }

    public void stop() {
        pool.stop();
    }
}