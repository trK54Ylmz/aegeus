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
package com.aegeus.utils;

import com.aegeus.config.ConfigParser;
import com.aegeus.config.format.ConfigObject;

import java.io.FileNotFoundException;

public class ConfigUtils {
    /**
     * Load configuration by yaml file
     *
     * @return Pojo based configuration object
     */
    public static ConfigObject getConfig() {
        try {
            ConfigObject config = ConfigParser.load(ConfigObject.class);
            if (config == null) {
                throw new RuntimeException("Configuration object could not find");
            }

            return config;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}