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
package com.aegeus.spark.input

import java.net.URLDecoder

import com.aegeus.schema.{Schema, SchemaField}
import com.aegeus.spark.config.ConfigObject
import com.aegeus.spark.`object`.CloudFrontLog
import com.aegeus.utils.ParameterUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.JavaConversions._
import scala.collection.mutable

class CloudFrontReader(sc: SparkContext, conf: ConfigObject, schemas: Map[String, Schema]) extends LogReader
{
  override def parse(): Unit = {
    val files: RDD[String] = sc.textFile(conf.data)
    val schemaKeys = schemas.map(_._2.getPath).toSeq

    val group = files.filter(!_.startsWith("#"))
      .map(p => new CloudFrontLog(p.split("[\\s]+")))
      .filter(p => p.resType.equals("Hit"))
      .groupBy(_.path)

    group.foreach { p =>
      val m = p._2.filter {
        r => schemaKeys.contains(r.path.substring(1))
      }.map { r =>
        val name = r.path.substring(1)
        val schema: Schema = schemas.get(name).get
        val fields: List[SchemaField] = schema.getFields.toList

        val params = Map(r.query.split('&')
          .map(_.split('='))
          .map { a => (URLDecoder.decode(a(0), "utf-8"), URLDecoder.decode(a(1), "utf-8")) }: _*)

        val row = mutable.ListBuffer[Any]()
        fields.foreach { f =>
          if (params.contains(f.getAlias)) {
            row add ParameterUtils.getObject(f.getType, params.getOrElse(f.getAlias, ""))
          } else {
            if (f.isNullable) {
              row add null
            } else if (f.getDef != null) {
              row add ParameterUtils.getObject(f.getType, f.getDef)
            }
          }
        }
        writer.writeValueAsString(row.toList)
      }

      sc.parallelize(m.toSeq).saveAsTextFile(conf.output + p._1 + "/")
    }

    es.close
  }
}