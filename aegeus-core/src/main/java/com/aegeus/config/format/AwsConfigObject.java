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

package com.aegeus.config.format;

import com.aegeus.config.Printable;

/**
 * Amazon web services configuration PoJo which contains S3 and EMR objects
 * <p/>
 * Properties can be listed with {@link #toString() toString} method
 */
public class AwsConfigObject extends Printable
{
    /* Aws s3 configuration object */
    private S3ConfigObject s3;

    /* Aws emr configuration object */
    private EmrConfigObject emr;

    public S3ConfigObject getS3() {
        return s3;
    }

    public void setS3(S3ConfigObject s3) {
        this.s3 = s3;
    }

    public EmrConfigObject getEmr() {
        return emr;
    }

    public void setEmr(EmrConfigObject emr) {
        this.emr = emr;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}