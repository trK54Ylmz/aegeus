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
package com.aegeus.engine.es

import java.util

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.mappings.TypedFieldDefinition

import scala.collection.mutable

class ElasticsearchClient {
  lazy val client = ElasticClient.remote("localhost", 9300)

  var types: Map[String, Class[_]] = _

  /**
    * Get type definition by name and instance type
    *
    * @param name Field name
    * @param ref  Field type
    * @tparam T Field type subtypes
    * @return The field definition type
    */
  protected def getType[T >: TypedFieldDefinition](name: String, ref: Class[_]): T = {
    var name = ref.getName.toLowerCase
    if (name.contains('.')) {
      val parts = name.split('.')
      name = parts(parts.length - 1)
    }

    name match {
      case "int" | "integer" => name typed IntegerType
      case "string" | "char" => name typed StringType
      case "long" => name typed LongType
      case "short" => name typed ShortType
      case "bool" | "boolean" => name typed BooleanType
      case "date" | "timestamp" => name typed DateType
    }
  }

  /**
    * Create fact index
    */
  def createIndices = {
    if (types == null) {
      throw new NullPointerException("Types map has empty value")
    }

    val indices: Iterable[TypedFieldDefinition] = new mutable.LinkedList[TypedFieldDefinition]()

    indices ++ List(getType("fid", classOf[String]))
    for (t <- types) {
      indices ++ List(getType(t._1, t._2))
    }

    client.execute {
      create index "facts" mappings (
        "fact" as indices
        )
    }.await
  }

  /**
    * Index or update fact
    *
    * @param fid  The fact id
    * @param data Fact table data.
    *             (e.g. {"uid" -> "12", "product_id" -> "AG-3SI98S", "search_id" -> "098f6bcd"})
    */
  def indexFact(fid: String, data: Map[String, Any]) = {
    val fact = getFact(fid)
    if (fact == null) {
      client.execute {
        index into "facts" -> "fact" id fid fields data
      }.await
    } else {
      client.execute {
        update id fid in "facts/fact" doc data
      }.await
    }
  }

  /**
    * Get fact row by fact id
    *
    * @param fid The fact id
    * @return
    */
  def getFact(fid: String): util.Map[String, AnyRef] = {
    client.execute(get id fid from "facts" -> "fact").await.getSource
  }

  def close = client.close()
}