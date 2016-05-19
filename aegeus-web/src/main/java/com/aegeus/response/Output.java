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
package com.aegeus.response;

import com.aegeus.util.RequestUtils;

import org.springframework.web.servlet.ModelAndView;

public class Output {
    /**
     * Get response object by view name, redirect path etc.
     *
     * @param name view name, redirect path with prefix
     * @return Returns model and view object by request name
     */
    public static ModelAndView print(String name) {
        if (name.startsWith("redirect:")) {
            return new ModelAndView(name);
        }

        return new ModelAndView(name).addAllObjects(RequestUtils.getProperties());
    }
}