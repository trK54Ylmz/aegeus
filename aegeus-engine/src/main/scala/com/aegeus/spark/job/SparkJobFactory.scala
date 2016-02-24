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
package com.aegeus.spark.job

import com.aegeus.spark.config.ConfigObject
import com.aegeus.utils.TimeUtils
import org.apache.spark.{SparkConf, SparkContext}

class SparkJobFactory(conf: ConfigObject)
{
  var sc: SparkContext = _

  def startClient: SparkJobFactory = {
    if (conf.master == null) {
      conf.master = "local"
    }

    if (conf.appName == null) {
      conf.appName = "aegeus-spark-" + TimeUtils.getString
    }

    val sparkConf = new SparkConf()
      .setMaster(conf.master)
      .setAppName(conf.appName)
      .set("es.nodes", "localhost")
      .set("es.nodes.discovery", "false")

    sc = new SparkContext(conf.master, conf.appName, sparkConf)

    this
  }
}