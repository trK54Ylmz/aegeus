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

package com.aegeus.spark.schema

import com.aegeus.spark.config.ConfigObject
import com.aegeus.config.format.S3ConfigObject

import scala.collection.JavaConversions._
import java.io.{File, IOException}

import com.aegeus.aws.SimpleStorageService
import com.aegeus.schema.{Schema, SchemaParser}
import com.aegeus.spark.utils.StringUtils

class SchemaReader(conf: ConfigObject)
{
  val s3conf = new S3ConfigObject

  /**
    * assign the s3 credentials and region
    */
  s3conf.setAccessKey(conf.s3Access)
  s3conf.setSecretKey(conf.s3Secret)
  s3conf.setRegion(conf.s3Region)

  val s3 = new SimpleStorageService(s3conf)

  val parser = new SchemaParser

  protected def checkDirIsEmpty(in: String): Unit = {
    if (StringUtils.isEmpty(in)) throw new NullPointerException("Input path directory is empty")
  }

  def list(paths: String): Map[String, Schema] = {
    checkDirIsEmpty(paths)

    val schemas = paths
      .split(",")
      .filter(p => p.endsWith("/") && p.startsWith("s3n://"))
      .map { p =>
        val schema = parser.loadFromS3(s3.getBucket(p), s3.getKey(p)).getSchema
        (schema.getTable, schema)
      }

    schemas.toMap
  }

  def s3(path: String): Map[String, Schema] = {
    checkDirIsEmpty(path)

    val files = s3.listObjects(s3.getBucket(path), s3.getKey(path))

    files.map { p =>
      val schema = parser.loadFromS3(p.getBucketName, p.getKey).getSchema
      (schema.getTable, schema)
    }.toMap
  }

  def local(path: String): Map[String, Schema] = {
    checkDirIsEmpty(path)

    if (path.contains(",")) {
      path.split(",")
        .filter(p => !p.endsWith("/"))
        .map { p =>
          val schema = parser.loadFromLocal(p).getSchema
          (schema.getTable, schema)
        }.toMap
    } else {
      val files = new File(path).list()

      files.filter(!_.endsWith("/"))
        .map { p =>
          val schema = parser.loadFromLocal(p).getSchema
          (schema.getTable, schema)
        }.toMap
    }
  }

  /**
    * Read templates from S3 or local disk
    *
    * @throws java.io.IOException Classic read exception from Java
    * @return Schema list based on Map type access
    */
  @throws[IOException]
  def read(): Map[String, Schema] = {
    val input = conf.schema

    {
      case x if conf.test => local(input)
      case x if input contains "," => list(input)
      case x if input startsWith "s3n://" => s3(input)
      case x => null
    }
  }
}