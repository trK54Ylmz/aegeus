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

package com.aegeus.util;

import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils
{
    private static final Logger logger = Logger.getLogger(RequestUtils.class);

    /**
     * Example usage:
     * <p/>
     * {@link RequestProperties}
     * {@link org.springframework.stereotype.Controller}
     * public class Main {
     * ...
     * }
     * <p/>
     * or
     * <p/>
     * {@link org.springframework.stereotype.Controller}
     * public class Main {
     * {@link RequestProperties}
     * public {@link org.springframework.web.servlet.ModelAndView} mainPage() {
     * ...
     * }
     * }
     *
     * @param <E> View name
     * @return Request properties
     */
    @SuppressWarnings("unchecked")
    public static <E> Map<String, E> getProperties() {
        String className = null, methodName = null;
        Map<String, E> props = new HashMap<>();

        /**
         * Receive current stack trace from current request
         */
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            /**
             * Filter the class name which is starts with controller package
             */
            if (element.getClassName().startsWith("com.aegeus.controller")) {
                className = element.getClassName();
                methodName = element.getMethodName();

                break;
            }
        }

        if (className != null && methodName != null) {
            try {
                Class cls = Class.forName(className);
                Annotation[] annotations = cls.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    /**
                     * Find {@link RequestProperties} annotation in declared annotations
                     */
                    if (annotation instanceof RequestProperties) {
                        RequestProperties curr = (RequestProperties) annotation;
                        props.put("name", (E) curr.name());
                        props.put("icon", (E) curr.icon());
                    }
                }


                Method[] methods = cls.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        annotations = method.getDeclaredAnnotations();

                        for (Annotation annotation : annotations) {
                            /**
                             * Find {@link RequestProperties} annotation in declared annotations
                             */
                            if (annotation instanceof RequestProperties) {
                                RequestProperties curr = (RequestProperties) annotation;
                                props.put("name", (E) curr.name());
                                props.put("icon", (E) curr.icon());

                                return props;
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return props;
    }
}