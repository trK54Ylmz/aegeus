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
package com.aegeus.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SessionUtils
{
    /**
     * Returns the current session status
     *
     * @return Returns TRUE if the guest has a user, otherwise FALSE
     */
    public static boolean hasUser() {
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            if (user.hasRole("admin") || user.hasRole("user")) {
                return true;
            }
        }

        return false;
    }
}