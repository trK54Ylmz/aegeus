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
package com.aegeus.core;

import com.aegeus.config.format.ConfigObject;
import com.aegeus.db.DbSessionFactory;
import com.aegeus.db.MySqlSessionFactory;
import com.aegeus.db.PostgreSqlSessionFactory;
import com.aegeus.utils.ConfigUtils;
import com.aegeus.utils.SchemaUtils;

import com.google.common.base.Preconditions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class WebAppBeans {
    private static final String SCHEMA_NAME = "com.aegeus.db.schema";

    @Bean
    public ConfigObject config() {
        return ConfigUtils.getConfig();
    }

    @Bean
    public DbSessionFactory factory() throws ClassNotFoundException, IOException {
        DbSessionFactory factory = null;
        ConfigObject config = config();

        switch (config.getDb().getType()) {
            case "mysql":
            case "memsql":
                factory = new MySqlSessionFactory(config.getDb(), SchemaUtils.getDepends(SCHEMA_NAME));
                break;

            case "postgres":
            case "redshift":
                factory = new PostgreSqlSessionFactory(config.getDb(), SchemaUtils.getDepends(SCHEMA_NAME));
                break;
        }

        Preconditions.checkNotNull(factory,
                "Database type is invalid. The database type must be `mysql`, `memsql`, `postgres` or `redshift`");

        return factory;
    }
}