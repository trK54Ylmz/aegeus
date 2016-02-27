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

package com.aegeus.db;

import com.aegeus.config.format.ConfigObject;
import org.apache.log4j.Logger;
import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.concurrent.Executors;

public class EmbeddedDataSource
{
    private static final Logger LOGGER = Logger.getLogger(EmbeddedDataSource.class);

    private Server server;

    public EmbeddedDataSource(ConfigObject config) {
        int port = config.getWorkflow().getDbPort();

        async(port);
    }

    /**
     * Create an synchronous database
     * <p/>
     * Configuration making based on workflow section
     * which is contains in configuration file
     *
     * @param port Tcp communication port
     */
    private void sync(int port) {
        try {
            LOGGER.info("Web database starting at " + port);

            /* Create tcp based h2 database instance */
            server = Server.createTcpServer("-tcpPort", String.valueOf(port),
                    "-baseDir", System.getProperty("java.io.tmpdir") + "/aegeus",
                    "-tcpAllowOthers").start();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Embedded database status detected as " + server.getStatus());
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Create an asynchronous database
     * <p/>
     * Configuration making based on workflow section
     * which is contains in configuration file
     *
     * @param port Tcp communication port
     */
    private void async(final int port) {
        /* Wrap method as async method */
        Executors.newSingleThreadExecutor().execute(new Runnable()
        {
            @Override
            public void run() {
                sync(port);
            }
        });
    }

    /**
     * This method basically for testing
     *
     * @param port    Tcp communication port
     * @param isAsync Database creation state
     */
    public void start(int port, boolean isAsync) {
        if (isAsync) async(port);
        else sync(port);
    }

    /**
     * Terminate the database
     */
    public void stop() {
        if (server.isRunning(false)) {
            server.stop();
        }
    }
}