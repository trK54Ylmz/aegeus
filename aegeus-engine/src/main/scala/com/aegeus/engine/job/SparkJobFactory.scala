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
import com.aegeus.engine.io.CloudFrontLog
import com.aegeus.schema.entity.{StarSchema, TableASchema}
import com.aegeus.utils.TimeUtils
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

class SparkJobFactory(conf: CliConfigObject) {
  var sc: SparkContext = _

  var sql: SQLContext = _

  def startContext: SparkJobFactory = {
    val master = if (conf.isTest) "local[*]" else "yarn-cluster"
    val appName = "aegeus-spark-" + TimeUtils.getFormat

    val sparkConf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)
      .set("es.nodes", "localhost")
      .set("es.nodes.discovery", "false")
      .set("es.index.auto.create", "true")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    // register job depend classes in kryo
    sparkConf.registerKryoClasses(Array(
      classOf[CloudFrontLog],
      classOf[StarSchema],
      classOf[TableASchema]))

    sc = new SparkContext(sparkConf)

    sql = new SQLContext(sc)
    sql.setConf("spark.sql.parquet.compression.codec", "snappy")
    sql.setConf("spark.sql.parquet.int96AsTimestamp", "true")

    this
  }
}