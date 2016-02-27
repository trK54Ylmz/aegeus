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
package com.aegeus.engine.config

import java.io.{FileNotFoundException, File, FileInputStream}

import org.yaml.snakeyaml.Yaml

object ConfigParser
{
  val config = new ConfigObject()

  val validator = new ConfigValidation(config)

  @throws[NullPointerException]
  private def file(): String = {
    val file = System.getProperty("config")
    if (file == null) {
      throw new NullPointerException("Please specify the configuration file with -Dconfig=[file]")
    }

    file
  }

  @throws[FileNotFoundException]
  def load[T] = {
    val file = this.file()
    new Yaml().load(new FileInputStream(new File(file))).asInstanceOf[T]
  }

  def load: ConfigObject = {
    config.appName = System.getProperty("appName")
    config.master = System.getProperty("master")
    config.s3Access = System.getProperty("s3Access")
    config.s3Secret = System.getProperty("s3Secret")
    config.s3Region = System.getProperty("s3Region")
    config.schema = System.getProperty("schema")
    config.data = System.getProperty("data")
    validator.validate()

    if (System.getProperty("test") != null) {
      config.test = System.getProperty("test").toBoolean
    }

    config
  }
}