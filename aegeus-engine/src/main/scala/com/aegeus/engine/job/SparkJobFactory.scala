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
package com.aegeus.engine.job

import com.aegeus.engine.config.format.CliConfigObject
import com.aegeus.utils.TimeUtils
import org.apache.spark.{SparkConf, SparkContext}

class SparkJobFactory(conf: CliConfigObject) {
  var sc: SparkContext = _

  def startContext: SparkJobFactory = {
    val master = if (conf.isTest) "local[*]" else ""
    val appName = "aegeus-spark-" + TimeUtils.getString

    val sparkConf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)
      .set("es.nodes", "localhost")
      .set("es.nodes.discovery", "false")

    sc = new SparkContext(sparkConf)

    this
  }
}