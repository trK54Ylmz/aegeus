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

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ConfigParser
{
    private static final Yaml yaml = new Yaml();

    public static <T> T load(Class<T> ref) throws NullPointerException, FileNotFoundException {
        String config = System.getProperty("config");
        if (config == null) {
            throw new NullPointerException("Please specify the path of configuration file via -Dconfig=[path]");
        }

        File file = new File(config);
        if (!file.exists()) {
            throw new FileNotFoundException("Configuration file could not be found");
        }

        return yaml.loadAs(new FileInputStream(file), ref);
    }
}