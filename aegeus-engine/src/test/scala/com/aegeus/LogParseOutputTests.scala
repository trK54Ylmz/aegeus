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
package com.aegeus

import java.net.URLDecoder
import java.util

import com.aegeus.schema.{Schema, SchemaField}
import com.aegeus.engine.log.CloudFrontLog
import com.aegeus.utils.ParameterUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.scalatest.FunSuite

import scala.collection.JavaConversions._
import scala.collection.mutable

class LogParseOutputTests extends FunSuite
{
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  val csvWriter = new CsvMapper

  val rows = Seq("#Version: 1.0\n",
    "#Fields: date time x-edge-location sc-bytes c-ip cs-method cs(Host) cs-uri-stem sc-status cs(Referer) cs(User-Agent) cs-uri-query cs(Cookie) x-edge-result-type x-edge-request-id x-host-header cs-protocol cs-bytes time-taken x-forwarded-for ssl-protocol ssl-cipher x-edge-response-result-type\n",
    "2014-05-23 01:13:11 FRA2 182 192.0.2.10 GET d111111abcdef8.cloudfront.net /i 200 www.displaymyfiles.com Mozilla/4.0%20(compatible;%20MSIE%205.0b1;%20Mac_PowerPC) u=Tarik&a=23&_i=true - RefreshHit MRVMF7KydIvxMWfJIglgwHQwZsbG2IhRJ07sn9AkKUFSHS9EXAMPLE== d111111abcdef8.cloudfront.net http - 0.001 - - - RefreshHit\n",
    "2014-05-23 01:13:12 LAX1 2390282 192.0.2.202 GET d111111abcdef8.cloudfront.net /i 304 www.unknownsingers.com Mozilla/4.0%20(compatible;%20MSIE%207.0;%20Windows%20NT%205.1) u=Tutankamon&a=1340&_i=true zip=50158 Hit xGN7KWpVEmB9Dp7ctcVFQC4E-nrcOcEKS3QyAez--06dV7TEXAMPLE== d111111abcdef8.cloudfront.net http - 0.002 - - - Hit\n")

  test("log parse with output test") {
    val username = new SchemaField
    username.setAlias("u")
    username.setName("username")
    username.setPrimary(false)
    username.setType("string")
    username.setPrimary(false)

    val age = new SchemaField
    age.setAlias("a")
    age.setName("age")
    age.setPrimary(false)
    age.setType("integer")
    age.setDef("0")
    age.setNullable(true)

    val isNew = new SchemaField
    isNew.setAlias("_i")
    isNew.setName("is_new")
    isNew.setDef("false")
    isNew.setNullable(true)
    isNew.setType("bool")

    val fields: util.List[SchemaField] = new util.ArrayList[SchemaField]()
    fields.add(username)
    fields.add(age)
    fields.add(isNew)

    val schema = new Schema
    schema.setActive(true)
    schema.setPath("i")
    schema.setTable("test-table")
    schema.setFields(fields)

    val beforeGroup = System.currentTimeMillis

    val schemas: Map[String, Schema] = Map("i" -> schema)
    val files = rows.mkString.split("\\n")
    val schemaKeys = schemas.map { s => s._2.getPath }.toSeq

    val group = files.filter(!_.startsWith("#"))
      .map(p => new CloudFrontLog(p.split("[\\s]+")))
      .groupBy(p => p.path)

    println("Group diff : " + (System.currentTimeMillis - beforeGroup))

    val beforeMap = System.currentTimeMillis
    group.foreach { p =>
      val m = p._2.map { r =>
        val name = r.path.substring(1)
        if (schemaKeys.contains(name)) {
          val schema: Schema = schemas.get(name).get
          val fields: List[SchemaField] = schema.getFields.toList

          val params = Map(r.query.split('&')
            .map { f => f.split('=') }
            .map { a => (URLDecoder.decode(a(0), "utf-8"), URLDecoder.decode(a(1), "utf-8")) }: _*)

          val row = mutable.ListBuffer[Any]()
          fields.foreach { f =>
            if (params.contains(f.getAlias)) {
              row.add(ParameterUtils.getObject(f.getType, params.getOrElse(f.getAlias, "")))
            } else {
              if (f.isNullable) {
                row.add(null)
              } else if (f.getDef != null) {
                row.add(ParameterUtils.getObject(f.getType, f.getDef))
              } else {
                throw new NullPointerException
              }
            }
          }
          mapper.writeValueAsString(row.toList)
        } else {
          None
        }
      }
    }

    println("Map diff : " + (System.currentTimeMillis - beforeMap))
  }
}
