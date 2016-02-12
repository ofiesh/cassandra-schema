package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.accessor.system.DescribeAccessor
import com.chowculator.cassandraschema.model.entity.Entity

class AutoSchema {
    final DescribeAccessor describe
    final Cql cql

    AutoSchema (DescribeAccessor describe, Cql cql) {
        this.describe = describe
        this.cql = cql
    }

    void createIfNotExists(String keyspace) {
        if(describe.getSchemaKeyspace(keyspace) == null) {
            cql.createKeyspace(keyspace)
        }
    }

    void generateColumns(Entity entity, String keyspace) {
        entity.columns.forEach {
            if(describe.getSchemaColumn(keyspace, entity.name, it.name) == null
                && !entity.partitionKeys.contains(it.name)
                && !entity.clusteringKeys.contains(it.name))
                cql.createColumn(it, entity.name, keyspace)
        }
    }

    void generateEntity(Entity entity,String  keyspace) {
        def table = describe.getSchemaColumnFamily(keyspace, entity.name)
        if(table == null)
            cql.createTable(entity, keyspace)
        else
            generateColumns(entity, keyspace)

    }

    void generate(String keyspace, Class... klasses) {
        createIfNotExists(keyspace)
        for(Class klass : klasses) {
            def entity = EntityProcessor.process(klass)
            generateEntity(entity, keyspace)
        }
    }

}
