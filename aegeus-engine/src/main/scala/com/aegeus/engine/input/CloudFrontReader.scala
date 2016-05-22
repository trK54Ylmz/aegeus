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
import com.aegeus.schema.SchemaContainer
import com.aegeus.schema.entity.{StarSchema, TableASchema}
import org.apache.spark.sql.{DataFrame, SQLContext}

class CloudFrontReader(sql: SQLContext, conf: CliConfigObject)
  extends LogReader[CloudFrontLog] {

  override def schemaGenerate(log: CloudFrontLog): SchemaContainer = {
    val star = new StarSchema
    val tableA = new TableASchema

    val container = new SchemaContainer()
    container.setStarSchema(star)
    container.setTableASchema(tableA)

    container
  }

  override def parse(): DataFrame = {
    val rdd = sql.sparkContext
      .textFile(conf.getInput)
      .filter(!_.startsWith("#"))
      .map(p => schemaGenerate(new CloudFrontLog(p.split("[\\s]+"))))

    sql.createDataFrame(rdd, classOf[SchemaContainer])
  }
}