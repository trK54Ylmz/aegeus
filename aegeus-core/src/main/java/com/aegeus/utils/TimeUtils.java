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
package com.aegeus.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.Date;

public class TimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Get current date as formatted string
     *
     * @return Formatted date
     */
    public static String getFormat() {
        return formatter.print(new Date().getTime());
    }

    /**
     * Get input date as formatted string
     *
     * @param date date object
     * @return Formatted date
     */
    public static String getFormat(Date date) {
        return formatter.print(date.getTime());
    }

    /**
     * Returns formatted date string by date and format string
     *
     * @param date   date object
     * @param format date format
     * @return formatted date
     */
    public static String getFormat(Date date, String format) {
        return DateTimeFormat.forPattern(format).print(date.getTime());
    }

    /**
     * Returns input string as Java date
     *
     * @param input formatted data string
     * @return data object
     */
    public static Date getDate(String input) {
        return formatter.parseDateTime(input).toDate();
    }

    /**
     * Returns input date as timestamp object
     *
     * @param input formatted date string
     * @return sql timestamp object
     */
    public static Timestamp getTime(String input) {
        return new Timestamp(formatter.parseDateTime(input).toDate().getTime());
    }
}