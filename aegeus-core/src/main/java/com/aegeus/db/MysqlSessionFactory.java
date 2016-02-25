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

import com.aegeus.config.format.DbConfigObject;

import java.util.List;

public class MysqlSessionFactory extends DbSessionFactory
{
    public MysqlSessionFactory(DbConfigObject config, List<Class> mapping) {
        DbIdentity identity = new DbIdentity();
        identity.setDriver("com.mysql.jdbc.Driver");
        identity.setDialect("org.hibernate.dialect.MySQLDialect");
        identity.setUrl(String.format("jdbc:mysql://%s:%d/%s", config.getHost(), config.getPort(), config.getDb()));
        identity.setUsername(config.getUsername());
        identity.setPassword(config.getPassword());

        build(identity, mapping);
    }
}