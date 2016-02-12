package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.connection.Connector
import com.chowculator.cassandraschema.model.entity.Entity
import com.chowculator.cassandraschema.model.entity.EntityColumn
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.SimpleStatement

class Cql {
    private final Connector connector

    Cql(Connector connector) {
        this.connector = connector
    }

    void createKeyspace(String keyspace) {
        connector.getSession().execute("""CREATE KEYSPACE ${keyspace}
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }""")
    }

    void dropKeyspace(String keyspace) {
        connector.getSession().execute("DROP KEYSPACE IF EXISTS ${keyspace}")
    }

    void createTable(Entity entity, String keyspace) {
        connector.getSession().execute(CreateEntityTable.toCql(entity, keyspace))
    }

    void createColumn(EntityColumn entityColumn, String tableName, String keyspace) {
        def stmt = new SimpleStatement(CreateEntityColumn.toCql(entityColumn, tableName, keyspace))
        stmt.setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
        connector.getSession().execute(stmt)
    }
}
