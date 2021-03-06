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
package com.aegeus.execution;

import com.aegeus.config.format.ConfigObject;

public class RootExecutor {
    private final String[] args;

    private final ConfigObject config;

    private final WebServerExecutor ws = new WebServerExecutor();

    public RootExecutor(String[] args, ConfigObject config) {
        this.args = args;
        this.config = config;
    }

    public void start() {
        ws.start(args, config);
    }
}