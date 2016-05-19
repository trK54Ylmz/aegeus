/*
 * Copyright 2015 Metglobal
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
package com.aegeus;

import com.aegeus.utils.TimeUtils;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeUtilsTest {
    @Test
    public void basicDateTest() {
        assertEquals(TimeUtils.getDate("2015-09-02 21:23:59").getTime(), new DateTime(2015, 9, 2, 21, 23, 59).toDate().getTime());
    }

    @Test
    public void basicTimeTest() {
        assertEquals(TimeUtils.getTime("2015-09-02 21:23:59").getTime(), new DateTime(2015, 9, 2, 21, 23, 59).toDate().getTime());
    }
}