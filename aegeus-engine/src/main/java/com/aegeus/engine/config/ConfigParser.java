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
package com.aegeus.engine.config;

import com.google.common.base.Preconditions;

import com.aegeus.config.IllegalValueException;
import com.aegeus.config.Printable;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {
    /**
     * Returns list of {@link ConfigFormat} annotations
     *
     * @param tClass PoJo object class type
     * @param <T>    generic class making reference to PoJo
     * @return command line parameters which are contains parameters and long-type parameters
     */
    private static <T extends Printable> List<ConfigFormat> getParameters(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();

        if (fields.length == 0) return null;

        List<ConfigFormat> parameters = new ArrayList<>();
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                // only ConfigFormat objects allowed
                if (annotation instanceof ConfigFormat) {
                    parameters.add((ConfigFormat) annotation);

                    break;
                }
            }
        }

        return parameters;
    }

    /**
     * Figures out field type and assign value to field
     *
     * @param instance  empty instance of current class
     * @param fieldName field name which need to be assigned
     * @param raw       value of command line argument
     * @param <T>       reference type of current class
     */
    private static <T extends Printable> void assignVariable(
            T instance,
            String fieldName,
            String raw) throws IllegalAccessException, IllegalValueException, InvocationTargetException, NoSuchMethodException {
        Preconditions.checkNotNull(fieldName, "Field name must not be null");

        // skip the assignment, if value of field is null
        if (raw == null) {
            return;
        }

        final Field[] fields = instance.getClass().getDeclaredFields();

        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                // only ConfigFormat objects allowed
                if (annotation instanceof ConfigFormat) {
                    final ConfigFormat cf = (ConfigFormat) annotation;

                    if (fieldName.equals(cf.name())) {

                        Class<?> tClass = field.getType();
                        Object castValue;

                        if (tClass == String.class) {
                            castValue = raw;
                        } else if (tClass == int.class || tClass == Integer.class) {
                            castValue = Integer.parseInt(raw);
                        } else if (tClass == byte.class || tClass == Byte.class) {
                            castValue = Byte.parseByte(raw);
                        } else if (tClass == short.class || tClass == Short.class) {
                            castValue = Short.parseShort(raw);
                        } else if (tClass == long.class || tClass == Long.class) {
                            castValue = Long.parseLong(raw);
                        } else if (tClass == char.class || tClass == Character.class) {
                            castValue = raw.charAt(0);
                        } else if (tClass == float.class || tClass == Float.class) {
                            castValue = Float.parseFloat(raw);
                        } else if (tClass == double.class || tClass == Double.class) {
                            castValue = Double.parseDouble(raw);
                        } else {
                            throw new IllegalValueException(String.format(
                                    "Invalid data type `%s` Only primitive types allowed at that moment.",
                                    tClass.getSimpleName()));
                        }

                        PropertyUtils.setProperty(instance, field.getName(), castValue);
                    }
                }
            }
        }
    }

    /**
     * Returns configuration object based on PoJo
     *
     * @param args   command line arguments
     * @param tClass PoJo object class type
     * @param <T>    generic class making reference to PoJo
     * @return configuration object
     */
    public static <T extends Printable> T getConfig(String[] args, Class<T> tClass)
            throws ParseException,
            InvocationTargetException,
            NoSuchMethodException,
            IllegalValueException,
            IllegalAccessException,
            InstantiationException {
        final List<ConfigFormat> parameters = getParameters(tClass);

        if (parameters == null || parameters.isEmpty()) {
            return null;
        }

        // set required command line arguments
        final Options options = new Options();
        for (ConfigFormat cf : parameters) {
            options.addOption(cf.name(), cf.arg(), cf.longName());
        }

        CommandLine cli = new BasicParser().parse(options, args);

        // create an empty instance of configuration object
        T instance = tClass.newInstance();

        for (ConfigFormat cf : parameters) {
            String raw = cli.getOptionValue(cf.name());

            assignVariable(instance, cf.name(), raw);
        }

        return instance;
    }
}