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
package com.aegeus.engine.input

import com.aegeus.engine.config.format.CliConfigObject
import com.aegeus.engine.io.CloudFrontLog
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class CloudFrontReader(sc: SparkContext, conf: CliConfigObject) extends LogReader {
  override def parse(): Unit = {
    val files: RDD[String] = sc.textFile(conf.getInput)

    val group = files.filter(!_.startsWith("#"))
      .map(p => new CloudFrontLog(p.split("[\\s]+")))
      .filter(p => p.resType.equals("Hit"))
      .groupBy(_.path)

    es.close
  }
}