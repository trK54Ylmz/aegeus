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
package com.aegeus.spark.config

import com.aegeus.spark.utils.StringUtils

class ConfigValidation(conf: ConfigObject)
{
  @throws[NullPointerException]
  @throws[IllegalArgumentException]
  def validate(): Unit = {
    def data() = {
      if (StringUtils.isEmpty(conf.data))
        throw new NullPointerException("Data input path is empty. Please use the -Ddata=[path] parameter")

      if (!conf.data.startsWith("s3n://"))
        throw new IllegalArgumentException("Data input protocol must be s3 protocol")
    }

    def schema() = {
      if (StringUtils.isEmpty(conf.schema))
        throw new NullPointerException("Schema source path is empty. Please use the -Dschema=[schema] parameter")

      if (!conf.data.startsWith("s3n://"))
        throw new IllegalArgumentException("Schema source protocol must be s3 protocol")
    }

    def s3() = {
      if (StringUtils.isEmptyAny(conf.s3Access, conf.s3Secret, conf.s3Region))
        throw new NullPointerException("Please specify aws credentials")
    }

    data()
    schema()
    s3()
  }
}