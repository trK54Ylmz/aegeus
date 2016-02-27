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

package com.aegeus;

import com.aegeus.config.ConfigParser;
import com.aegeus.config.ConfigValidation;
import com.aegeus.config.format.ConfigObject;
import com.aegeus.execution.RootExecutor;
import org.apache.log4j.Logger;

public class WebServer
{
    private static final Logger LOGGER = Logger.getLogger(WebServer.class);

    public static void main(String[] args) {
        try {
            ConfigObject config = ConfigParser.load(ConfigObject.class);
            if (config == null) {
                return;
            }

            final RootExecutor executor = new RootExecutor(args, new ConfigValidation(config).validate());

            /* add ctrl-c handler */
            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run() {
                    executor.stop();
                }
            });

            executor.start();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
        }
    }
}