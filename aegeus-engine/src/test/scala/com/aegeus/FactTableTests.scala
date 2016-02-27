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

import java.util.Date

import com.aegeus.engine.es.ElasticsearchClient
import org.joda.time.DateTime
import org.scalatest.FunSuite

class FactTableTests extends FunSuite
{
  val es = new ElasticsearchClient
  es.types = Map("user_id" -> classOf[Int], "search_id" -> classOf[String], "add_time" -> classOf[Date])

  test("crete index") {
    es.createIndices
  }

  test("index fact data") {
    es.indexFact("test", Map("user_id" -> 23, "search_id" -> "AG-7B3OX6", "add_time" -> DateTime.now))
  }
}