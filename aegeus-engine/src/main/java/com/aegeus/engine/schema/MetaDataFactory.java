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
package com.aegeus.engine.schema;

import com.aegeus.schema.Schema;
import com.aegeus.schema.SchemaContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetaDataFactory {
    /**
     * Returns output folder names through {@link Schema} annotation
     *
     * @return folder name list
     */
    public static List<String> getFields() {
        List<String> schemaNames = new ArrayList<>();

        Field[] fields = SchemaContainer.class.getDeclaredFields();

        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Schema) {
                    schemaNames.add(((Schema) annotation).name());
                }
            }
        }

        return schemaNames;
    }
}