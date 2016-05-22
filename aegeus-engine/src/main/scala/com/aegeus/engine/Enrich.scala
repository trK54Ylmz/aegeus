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
package com.aegeus.engine

import com.aegeus.engine.config.ConfigParser
import com.aegeus.engine.config.format.CliConfigObject
import com.aegeus.engine.input.CloudFrontReader
import com.aegeus.engine.job.SparkJobFactory
import com.aegeus.engine.schema.MetaDataFactory
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._

import _root_.scala.collection.JavaConversions._

object Enrich extends Job {
  def main(args: Array[String]) {
    val config = ConfigParser.getConfig(args, classOf[CliConfigObject])

    val job = new SparkJobFactory(config).startContext

    try {
      val input = new CloudFrontReader(job.sql, config)

      // creates data frame through log files
      val df = input.parse()

      /**
        * get output directory names by [[com.aegeus.schema.Schema]] annotation
        *
        * schema annotation defines output folder name
        */
      val fields = MetaDataFactory.getFields

      // iterate all over schema list and write each schema to output folder
      for (schema <- fields) {
        df.select(col(schema))
          .write
          .mode(SaveMode.Overwrite)
          .parquet(s"${config.getOutput}/${getPartition(config.getTime)}/$schema")
      }
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
  }
}