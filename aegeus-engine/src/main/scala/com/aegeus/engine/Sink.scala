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
import com.aegeus.engine.job.{FactTableWriter, SparkJobFactory, StorageWriter}
import org.apache.log4j.Logger

object Sink {
  private[this] lazy val logger: Logger = Logger.getLogger(this.getClass.getName)

  def main(args: Array[String]) {
    val config = ConfigParser.getConfig(args, classOf[CliConfigObject])

    val job = new SparkJobFactory(config).startContext

    try {
      /**
        * Save fact table from Elasticsearch to storage (local or s3)
        * over Spark connector
        */
      val fact = new FactTableWriter(job.sc, config)
      fact.write()

      val storage = new StorageWriter(job.sc, config)
      storage.write()
    } catch {
      case e: Throwable => logger.error(e.getMessage, e)
    }
  }
}