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

import com.aegeus.spark.es.ElasticsearchClient
import org.scalatest.FunSuite

class EsTests extends FunSuite
{
  val es = new ElasticsearchClient
  es.types = Map("uid" -> classOf[Int], "search_id" -> classOf[String], "product_id" -> classOf[String])
  es.createIndices

  val fid = "123456"

  test("index and update") {
    val map = Map[String, Any]("uid" -> "12", "product_id" -> "AG-3SI98S")
    es.indexFact(fid, map)

    println(es.getFact(fid))

    val updateMap = Map[String, Any]("search_id" -> "098f6bcd")
    es.indexFact(fid, updateMap)

    println(es.getFact(fid))
  }
}