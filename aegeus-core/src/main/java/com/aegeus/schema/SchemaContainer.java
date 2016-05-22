/*
 * Copyright 2016 Tarık Yılmaz
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
package com.aegeus.schema;

import com.aegeus.schema.entity.StarSchema;
import com.aegeus.schema.entity.TableASchema;

import java.io.Serializable;

public class SchemaContainer implements Serializable {
    @Schema(name = "fact_table")
    private StarSchema starSchema;

    @Schema(name = "table_a")
    private TableASchema tableASchema;

    public StarSchema getStarSchema() {
        return starSchema;
    }

    public void setStarSchema(StarSchema starSchema) {
        this.starSchema = starSchema;
    }

    public TableASchema getTableASchema() {
        return tableASchema;
    }

    public void setTableASchema(TableASchema tableASchema) {
        this.tableASchema = tableASchema;
    }
}