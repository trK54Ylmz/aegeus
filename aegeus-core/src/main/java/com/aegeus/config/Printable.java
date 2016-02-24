/*
 * Copyright 2016 Tarık Yılmaz
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

import com.google.common.base.MoreObjects;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

public abstract class Printable
{
    private static final Logger logger = Logger.getLogger(Printable.class);

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this.getClass().getName());

        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                helper.add(field.getName(), field.get(this));
            }
        } catch (IllegalAccessException e) {
            logger.error(e);
        }

        return helper.toString();
    }
}