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

import com.aegeus.engine.log.CloudFrontLog
import org.scalatest.FunSuite

class LogParseTests extends FunSuite
{
  val row = "2014-05-23 01:13:11 FRA2 182 192.0.2.10 GET d111111abcdef8.cloudfront.net /view/my/file.html 200 www.displaymyfiles.com Mozilla/4.0%20(compatible;%20MSIE%205.0b1;%20Mac_PowerPC) - zip=98101 RefreshHit MRVMF7KydIvxMWfJIglgwHQwZsbG2IhRJ07sn9AkKUFSHS9EXAMPLE== d111111abcdef8.cloudfront.net http - 0.001 - - - RefreshHit"

  test("row split") {
    val cfLog = new CloudFrontLog(row.mkString.split("[\\s]+"))

    println(cfLog)
    assert(cfLog.date.equals("2014-05-23"))
  }
}