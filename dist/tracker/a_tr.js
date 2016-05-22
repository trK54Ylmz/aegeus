/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function () {
    /**
     * Aegeus tracker
     *
     * @type {{tracker}}
     * @version 0.1
     * @private
     */
    var tracker = {};

    /**
     * Aegeus global variable
     *
     * @type {{object}}
     */
    window.aegeus = {};

    /**
     * Get parameter from query string
     *
     * @param param {string} Query parameter name
     * @returns {string|null}
     */
    tracker.getParameter = function (param) {
        if (!param) {
            return null;
        }

        /* get query string */
        var queryStr = window.location.search.substring(1);

        /* split by & char */
        var qsp = queryStr.split("&");
        for (var i = 0; i < qsp.length; i++) {
            var qsv = qsp[i].split("=");
            if (qsv[0] == param) {
                return qsv[1];
            }
        }

        return null;
    };

    /**
     * Get cookie by name
     *
     * @param name {string} Cookie name
     * @returns {string|null}
     */
    tracker.getCookie = function (name) {
        if (!name) {
            return null;
        }
        return decodeURIComponent(document.cookie.replace(
                new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(name)
                        .replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1"))
               || null;

    };

    /**
     * Set cookie
     *
     * @param k {string} Cookie name
     * @param v {string} Cookie value
     * @param t {Number} Time to live in secs
     * @param p {string} Cookie path
     * @param d {string} Cookie domain
     * @param iss {boolean} HTTP Cookie
     *
     * @returns boolean
     */
    tracker.setCookie = function (k, v, t, p, d, iss) {
        if (!k || /^(?:expires|max\-age|path|domain|secure)$/i.test(k)) {
            return false;
        }

        var exp = "";
        if (t) {
            switch (t.constructor) {
                case Number:
                    exp =
                        t === Infinity ? "; expires=Fri, 31 Dec 9999 23:59:59 GMT" : "; max-age="
                                                                                     + t;
                    break;
                case String:
                    exp = "; expires=" + t;
                    break;
                case Date:
                    exp = "; expires=" + t.toUTCString();
                    break;
            }
        }
        document.cookie =
            encodeURIComponent(k) + "=" + encodeURIComponent(v) + exp + (d ? "; domain=" + d : "")
            + (p ? "; path=" + p : "") + (iss ? "; secure" : "");
        return true;
    };

    /**
     * Delete cookie by name
     *
     * @param k {string} Cookie name
     * @param p {string} Cookie path
     * @param d {string} Cookie domain
     *
     * @returns {boolean}
     */
    tracker.deleteCookie = function (k, p, d) {
        if (!this.cookieExists(k)) {
            return false;
        }
        document.cookie =
            encodeURIComponent(k) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + (d ? "; domain="
            + d : "") + (p ? "; path=" + p : "");
        return true;
    };

    /**
     * Check cookie if exists
     *
     * @param key {string} Cookie name
     *
     * @returns {boolean}
     */
    tracker.cookieExists = function (key) {
        if (!key) {
            return false;
        }
        return (new RegExp("(?:^|;\\s*)" + encodeURIComponent(key).replace(/[\-\.\+\*]/g, "\\$&")
                           + "\\s*\\=")).test(document.cookie);
    };

    /**
     * Get the cookie name list
     *
     * @returns {Array}
     */
    tracker.getCookieNames = function () {
        var ky = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g,
                                         "").split(/\s*(?:\=[^;]*)?;\s*/);
        for (var ln = ky.length, i = 0; i < ln; i++) {
            ky[i] = decodeURIComponent(ky[i]);
        }
        return ky;
    };

    /**
     * ASCII characters
     *
     * @type {string}
     */
    tracker.ascii = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    /**
     * Base64 encoder
     *
     * @param e {string}
     *
     * @returns {string}
     */
    tracker.base64Encode = function (e) {
        var t = "";
        var n, r, i, s, o, u, a;
        var f = 0;
        e = this.utfEncode(e);
        while (f < e.length) {
            n = e.charCodeAt(f++);
            r = e.charCodeAt(f++);
            i = e.charCodeAt(f++);
            s = n >> 2;
            o = (n & 3) << 4 | r >> 4;
            u = (r & 15) << 2 | i >> 6;
            a = i & 63;
            if (isNaN(r)) {
                u = a = 64
            } else if (isNaN(i)) {
                a = 64
            }
            t =
                t + this.ascii.charAt(s) + this.ascii.charAt(o) + this.ascii.charAt(u)
                + this.ascii.charAt(a)
        }
        return t;
    };

    /**
     * Base64 decoder
     *
     * @param e {string}
     *
     * @returns {string}
     */
    tracker.base64Decode = function (e) {
        var t = "";
        var n, r, i;
        var s, o, u, a;
        var f = 0;
        e = e.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (f < e.length) {
            s = this.ascii.indexOf(e.charAt(f++));
            o = this.ascii.indexOf(e.charAt(f++));
            u = this.ascii.indexOf(e.charAt(f++));
            a = this.ascii.indexOf(e.charAt(f++));
            n = s << 2 | o >> 4;
            r = (o & 15) << 4 | u >> 2;
            i = (u & 3) << 6 | a;
            t = t + String.fromCharCode(n);
            if (u != 64) {
                t = t + String.fromCharCode(r)
            }
            if (a != 64) {
                t = t + String.fromCharCode(i)
            }
        }
        t = this.utfEncode(t);
        return t;
    };

    /**
     * Encode utf-8 encode
     *
     * @param e {string}
     *
     * @returns {string}
     */
    tracker.utfEncode = function (e) {
        e = e.replace(/\r\n/g, "\n");
        var t = "";
        for (var n = 0; n < e.length; n++) {
            var r = e.charCodeAt(n);
            if (r < 128) {
                t += String.fromCharCode(r)
            } else if (r > 127 && r < 2048) {
                t += String.fromCharCode(r >> 6 | 192);
                t += String.fromCharCode(r & 63 | 128)
            } else {
                t += String.fromCharCode(r >> 12 | 224);
                t += String.fromCharCode(r >> 6 & 63 | 128);
                t += String.fromCharCode(r & 63 | 128)
            }
        }
        return t;
    };

    /**
     * Decode utf-8 string
     *
     * @param e {string}
     *
     * @returns {string}
     */
    tracker.utfDecode = function (e) {
        var t = "";
        var n = 0;
        var r = 0, c3 = 0, c2 = 0;
        while (n < e.length) {
            r = e.charCodeAt(n);
            if (r < 128) {
                t += String.fromCharCode(r);
                n++
            } else if (r > 191 && r < 224) {
                c2 = e.charCodeAt(n + 1);
                t += String.fromCharCode((r & 31) << 6 | c2 & 63);
                n += 2
            } else {
                c2 = e.charCodeAt(n + 1);
                c3 = e.charCodeAt(n + 2);
                t += String.fromCharCode((r & 15) << 12 | (c2 & 63) << 6 | c3 & 63);
                n += 3
            }
        }
        return t;
    };

    /**
     * Get screen resolution width
     *
     * @returns {Number}
     */
    tracker.getWidth = function () {
        return window.screen.width;
    };

    /**
     * Get screen resolution height
     *
     * @returns {Number}
     */
    tracker.getHeight = function () {
        return window.screen.height;
    };

    /**
     * Timezone default config
     *
     * @type {Object}
     */
    tracker.tzConf = {
        day: 86400000,
        hour: 3600000,
        min: 60000,
        sec: 1000,
        year: 2014,
        max: 864000000,
        unk: {
            'America/Denver': ['America/Mazatlan'],
            'America/Chicago': ['America/Mexico_City'],
            'America/Santiago': ['America/Asuncion', 'America/Campo_Grande'],
            'America/Montevideo': ['America/Sao_Paulo'],
            'Asia/Beirut': ['Asia/Amman', 'Asia/Jerusalem', 'Europe/Helsinki', 'Asia/Damascus',
                            'Africa/Cairo', 'Asia/Gaza', 'Europe/Minsk'],
            'Pacific/Auckland': ['Pacific/Fiji'],
            'America/Los_Angeles': ['America/Santa_Isabel'],
            'America/New_York': ['America/Havana'],
            'America/Halifax': ['America/Goose_Bay'],
            'America/Godthab': ['America/Miquelon'],
            'Asia/Dubai': ['Asia/Yerevan'],
            'Asia/Jakarta': ['Asia/Krasnoyarsk'],
            'Asia/Shanghai': ['Asia/Irkutsk', 'Australia/Perth'],
            'Australia/Sydney': ['Australia/Lord_Howe'],
            'Asia/Tokyo': ['Asia/Yakutsk'],
            'Asia/Dhaka': ['Asia/Omsk'],
            'Asia/Baku': ['Asia/Yerevan'],
            'Australia/Brisbane': ['Asia/Vladivostok'],
            'Pacific/Noumea': ['Asia/Vladivostok'],
            'Pacific/Majuro': ['Asia/Kamchatka', 'Pacific/Fiji'],
            'Pacific/Tongatapu': ['Pacific/Apia'],
            'Asia/Baghdad': ['Europe/Minsk', 'Europe/Moscow'],
            'Asia/Karachi': ['Asia/Yekaterinburg'],
            'Africa/Johannesburg': ['Asia/Gaza', 'Africa/Cairo']
        }
    };

    /**
     * Get timezone name by offset value
     *
     * @param ofs {Number}
     * @returns {string|null}
     */
    tracker.getTzOffsetByValue = function (ofs) {
        var ofsl = {
            '-720,0': 'Etc/GMT+12',
            '-660,0': 'Pacific/Pago_Pago',
            '-660,1,s': 'Pacific/Apia',
            '-600,1': 'America/Adak',
            '-600,0': 'Pacific/Honolulu',
            '-570,0': 'Pacific/Marquesas',
            '-540,0': 'Pacific/Gambier',
            '-540,1': 'America/Anchorage',
            '-480,1': 'America/Los_Angeles',
            '-480,0': 'Pacific/Pitcairn',
            '-420,0': 'America/Phoenix',
            '-420,1': 'America/Denver',
            '-360,0': 'America/Guatemala',
            '-360,1': 'America/Chicago',
            '-360,1,s': 'Pacific/Easter',
            '-300,0': 'America/Bogota',
            '-300,1': 'America/New_York',
            '-270,0': 'America/Caracas',
            '-240,1': 'America/Halifax',
            '-240,0': 'America/Santo_Domingo',
            '-240,1,s': 'America/Santiago',
            '-210,1': 'America/St_Johns',
            '-180,1': 'America/Godthab',
            '-180,0': 'America/Argentina/Buenos_Aires',
            '-180,1,s': 'America/Montevideo',
            '-120,0': 'America/Noronha',
            '-120,1': 'America/Noronha',
            '-60,1': 'Atlantic/Azores',
            '-60,0': 'Atlantic/Cape_Verde',
            '0,0': 'UTC',
            '0,1': 'Europe/London',
            '60,1': 'Europe/Berlin',
            '60,0': 'Africa/Lagos',
            '60,1,s': 'Africa/Windhoek',
            '120,1': 'Asia/Beirut',
            '120,0': 'Africa/Johannesburg',
            '180,0': 'Asia/Baghdad',
            '180,1': 'Europe/Moscow',
            '210,1': 'Asia/Tehran',
            '240,0': 'Asia/Dubai',
            '240,1': 'Asia/Baku',
            '270,0': 'Asia/Kabul',
            '300,1': 'Asia/Yekaterinburg',
            '300,0': 'Asia/Karachi',
            '330,0': 'Asia/Kolkata',
            '345,0': 'Asia/Kathmandu',
            '360,0': 'Asia/Dhaka',
            '360,1': 'Asia/Omsk',
            '390,0': 'Asia/Rangoon',
            '420,1': 'Asia/Krasnoyarsk',
            '420,0': 'Asia/Jakarta',
            '480,0': 'Asia/Shanghai',
            '480,1': 'Asia/Irkutsk',
            '525,0': 'Australia/Eucla',
            '525,1,s': 'Australia/Eucla',
            '540,1': 'Asia/Yakutsk',
            '540,0': 'Asia/Tokyo',
            '570,0': 'Australia/Darwin',
            '570,1,s': 'Australia/Adelaide',
            '600,0': 'Australia/Brisbane',
            '600,1': 'Asia/Vladivostok',
            '600,1,s': 'Australia/Sydney',
            '630,1,s': 'Australia/Lord_Howe',
            '660,1': 'Asia/Kamchatka',
            '660,0': 'Pacific/Noumea',
            '690,0': 'Pacific/Norfolk',
            '720,1,s': 'Pacific/Auckland',
            '720,0': 'Pacific/Majuro',
            '765,1,s': 'Pacific/Chatham',
            '780,0': 'Pacific/Tongatapu',
            '780,1,s': 'Pacific/Apia',
            '840,0': 'Pacific/Kiritimati'
        };

        if (ofsl.hasOwnProperty(ofs.toString())) {
            return ofsl[ofs];
        }
        return null;
    };

    /**
     * Get timezone offset
     *
     * @returns {number}
     */
    tracker.getTzOffset = function () {
        var ofs = -new Date().getTimezoneOffset();
        return (ofs !== null ? ofs : 0);
    };

    /**
     *
     * @param values
     */
    window.aegeus.push = function (values) {

    }
})(window);