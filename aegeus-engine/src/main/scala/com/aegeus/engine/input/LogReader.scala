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

import com.aegeus.engine.es.ElasticsearchClient
import com.aegeus.engine.io.Log
import com.aegeus.schema.SchemaContainer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.spark.sql.DataFrame

trait LogReader[E <: Log] {
  val es = new ElasticsearchClient
  es.createIndices

  val writer = new ObjectMapper() with ScalaObjectMapper
  writer.registerModule(DefaultScalaModule)

  def schemaGenerate(log: E): SchemaContainer = ???

  def parse(): DataFrame = ???
}