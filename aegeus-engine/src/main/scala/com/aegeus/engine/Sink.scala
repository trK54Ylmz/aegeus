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

import com.aegeus.engine.config.{ConfigParser, ConfigObject}
import com.aegeus.engine.job.{StorageWriter, FactTableWriter, SparkJobFactory}
import com.aegeus.engine.schema.SchemaReader
import org.apache.log4j.Logger

object Sink
{
  private[this] lazy val logger: Logger = Logger.getLogger(this.getClass.getName)

  val config = ConfigParser.load[ConfigObject]

  val job = new SparkJobFactory(config).startClient

  def main(args: Array[String]) {
    try {
      val schema = new SchemaReader(config).read()

      /**
        * Save fact table from Elasticsearch to storage (local or s3)
        * over Spark connector
        */
      val fact = new FactTableWriter(job.sc, config)
      fact.write()

      val storage = new StorageWriter(job.sc, config, schema)
      storage.write()
    } catch {
      case e: Throwable => logger.error(e.getMessage, e)
    }
  }
}