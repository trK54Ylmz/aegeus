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
package com.aegeus.executor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final Logger logger = LogManager.getLogger(ThreadPool.class);

    private ThreadPoolExecutor executor;

    private int coreSize;

    private int queueSize;

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public void createThreadPool() {
        final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);

        executor = new ThreadPoolExecutor(coreSize, coreSize, 1L, TimeUnit.HOURS, queue);
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    Thread.sleep(2000);
                    queue.put(r);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    public int getActiveThread() {
        return executor.getQueue().size();
    }

    public void execute(Runnable r) {
        executor.execute(r);
    }

    public <T> T submit(Runnable runnable, T type) {
        try {
            Future<T> future = executor.submit(runnable, type);
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public void stop() {
        executor.shutdown();
    }
}