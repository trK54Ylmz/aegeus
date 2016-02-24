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
package com.aegeus.controller;

import com.aegeus.response.Output;
import com.aegeus.util.RequestProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogInController
{
    @RequestProperties(name = "Login")
    @RequestMapping(value = "/login", method = GET)
    public ModelAndView showLogInPage() {
        /**
         * Check the user authentication status
         */
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            if (user.hasRole("admin") || user.hasRole("user")) {
                return Output.print("redirect:/");
            }
        }

        return Output.print("login");
    }

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView login(@RequestParam(value = "username") String username,
                              @RequestParam(value = "password") String password) {
        if (StringUtils.isAnyEmpty(username, password)) {
            return null;
        }

        return null;
    }
}