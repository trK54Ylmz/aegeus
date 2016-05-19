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
package com.aegeus.engine.config.format;

import com.aegeus.config.Printable;
import com.aegeus.engine.config.ConfigFormat;

public class CliConfigObject extends Printable {
    @ConfigFormat(name = "i", longName = "input")
    private String input;

    @ConfigFormat(name = "o", longName = "output")
    private String output;

    @ConfigFormat(name = "a", longName = "accessKey")
    private String accessKey;

    @ConfigFormat(name = "s", longName = "secretKey")
    private String secretKey;

    @ConfigFormat(name = "r", longName = "region")
    private String regionKey;

    @ConfigFormat(name = "t", longName = "test", arg = false)
    private boolean test;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegionKey() {
        return regionKey;
    }

    public void setRegionKey(String regionKey) {
        this.regionKey = regionKey;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}