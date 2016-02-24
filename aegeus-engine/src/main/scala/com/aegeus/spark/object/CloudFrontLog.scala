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
package com.aegeus.spark.`object`

import org.apache.commons.lang3.builder.ToStringBuilder

/**
  * CloudFront log syntax
  *
  * date time x-edge-location sc-bytes c-ip cs-method cs(Host) cs-uri-stem sc-status cs(Referer) cs(User-Agent) cs-uri-query
  * cs(Cookie) x-edge-result-type x-edge-request-id x-host-header cs-protocol cs-bytes time-taken x-forwarded-for ssl-protocol
  * ssl-cipher x-edge-response-result-type
  */
class CloudFrontLog(cols: Array[String])
{
  var date: String = cols(0)
  var time: String = cols(1)
  var location: String = cols(2)
  var byte: Int = cols(3).toInt
  var ip: String = cols(4)
  var method: String = cols(5)
  var url: String = cols(6)
  var path: String = cols(7)
  var response: Short = cols(8).toShort
  var referrer: String = cols(9)
  var ua: String = cols(10)
  var query: String = cols(11)
  var cookie: String = cols(12)
  var result: String = cols(13)
  var rid: String = cols(14)
  var host: String = cols(15)
  var scheme: String = cols(16)
  var bytes: String = cols(17)
  var taken: Float = cols(18).toFloat
  var forwarded: String = cols(19)
  var ssl: String = cols(20)
  var cipher: String = cols(21)
  var resType: String = cols(22)

  override def toString = ToStringBuilder.reflectionToString(this)
}