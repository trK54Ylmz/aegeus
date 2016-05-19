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
package com.aegeus.schema.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class TableA extends Schema {
    @Id
    @Column(name = "fact_id", unique = true, nullable = false)
    private String factId;

    @Column(name = "a_id")
    private String aId;

    @Column
    private String s1;

    @Column
    private String s2;

    @Column
    private String s3;

    @Column
    private String s4;

    @Column
    private String s5;

    @Column
    private String s6;

    @Column
    private String s7;

    @Column
    private String s8;

    @Column
    private String s9;

    @Column
    private String s10;

    @Column
    private String s11;

    @Column
    private String s12;

    @Column
    private String s13;

    @Column
    private String s14;

    @Column
    private String s15;

    @Column
    private String s16;

    @Column
    private String s17;

    @Column
    private String s18;

    @Column
    private String s19;

    @Column
    private String s20;


}
