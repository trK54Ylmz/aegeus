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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;

public abstract class DbSessionFactory
{
    protected SessionFactory factory;

    public Session getSession() {
        Session s = factory.openSession();

        if (s == null || !s.isOpen() || !s.isConnected()) {
            s = factory.openSession();
        }

        return s;
    }

    protected void build(DbIdentity identity, List<Class> pojoGroup) {
        Configuration cfg = new Configuration();
        cfg.setProperty("hibernate.connection.driver", identity.getDriver())
                .setProperty("hibernate.dialect", identity.getDialect())
                .setProperty("hibernate.connection.url", identity.getUrl())
                .setProperty("hibernate.connection.username", identity.getUsername())
                .setProperty("hibernate.connection.password", identity.getPassword())
                .setProperty("hibernate.connection.CharSet", "utf-8")
                .setProperty("hibernate.connection.characterEncoding", "utf-8")
                .setProperty("hibernate.connection.useUnicode", "true")
                .setProperty("current_session_context_class", "thread")
                .setProperty("connection.pool_size", "4")
                .setProperty("hibernate.show_sql", "true");

        for (Class pojo : pojoGroup) {
            cfg.addAnnotatedClass(pojo);
        }

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        factory = cfg.buildSessionFactory(builder.build());
    }

    public void close() {
        factory.close();
    }
}