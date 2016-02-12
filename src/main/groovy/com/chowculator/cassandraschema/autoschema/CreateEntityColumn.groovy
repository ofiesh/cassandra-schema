package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.EntityColumn

class CreateEntityColumn {
    static toCql(EntityColumn column, String tableName, String keyspace) {
        "ALTER TABLE ${keyspace}.${tableName} ADD ${column.name} ${column.type}"
    }
}
