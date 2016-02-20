package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.Entity

class CreateEntityTable {
    static toCql(Entity entity, String keyspace) {
        String cql = "CREATE TABLE ${keyspace}.${entity.name} ( "
        entity.columns.forEach {
            cql += "${it.name} ${it.type}, "
        }
        cql += "PRIMARY KEY (("
        entity.partitionKeys.forEach {
            cql += it
            if(it != entity.partitionKeys.last())
                cql += ", "
        }
        cql += ")"
        if(entity.clusteringKeys != null && entity.clusteringKeys.size() > 0) {
            cql += ", "
            entity.clusteringKeys.forEach {
                cql += it
                if(it != entity.clusteringKeys.last())
                    cql += ", "
            }
        }
        cql += ") )"
    }
}
