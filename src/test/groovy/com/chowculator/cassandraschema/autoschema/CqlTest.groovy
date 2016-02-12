package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.accessor.system.DescribeAccessor
import com.chowculator.cassandraschema.connection.Connector
import com.chowculator.cassandraschema.connection.ConnectorConfig
import com.chowculator.cassandraschema.model.entity.EntityColumn
import com.datastax.driver.mapping.MappingManager
import com.datastax.driver.mapping.annotations.ClusteringColumn
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import com.datastax.driver.mapping.annotations.Transient
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class CqlTest {
    static Connector connector
    static Cql cql
    static DescribeAccessor describe

    @BeforeClass
    static void beforeClass() {
        ConnectorConfig connectorConfig = new ConnectorConfig(host: "localhost", port: 9042, keyspace: "system")
        connector = new Connector(connectorConfig)
        cql = new Cql(connector)
        describe = new MappingManager(connector.session).createAccessor(DescribeAccessor.class)
        cql.dropKeyspace("test_keyspace")
        cql.createKeyspace("test_keyspace")
    }

    @AfterClass
    static void afterClass() {
        connector.session.close()
    }

    @Test
    void testCreateKeyspace() {
        String ks = "cql_test_keyspace"
        cql.createKeyspace(ks)
        assert describe.getSchemaKeyspace(ks).keyspaceName == ks
        cql.dropKeyspace(ks)
        assert describe.getSchemaKeyspace(ks) == null
    }

    @Table(name = "foo")
    static class Foo {
        @PartitionKey(0)
        String bar

        @PartitionKey(1)
        BigDecimal foo

        @ClusteringColumn
        String cluster

        String norm

        @Column(name = "foo_bar")
        Integer fooBar

        @Transient
        String no
    }

    @Test
    void testCreateTable() {
        connector.session.execute("DROP TABLE IF EXISTS test_keyspace.foo")
        def ks = "test_keyspace"
        cql.createTable(EntityProcessor.process(Foo), ks)
        assert "foo" == describe.getSchemaColumnFamily(ks, "foo").columnFamilyName
        assert "norm" == describe.getSchemaColumn(ks, "foo", "norm").columnName
        assert null == describe.getSchemaColumn(ks, "foo", "no")
        assert "foo_bar" == describe.getSchemaColumn(ks, "foo", "foo_bar").columnName
        connector.session.execute("DROP TABLE test_keyspace.foo")
    }

    @Test
    void testCreateColumn() {
        connector.session.execute("DROP TABLE IF EXISTS test_keyspace.foo")
        connector.session.execute("CREATE TABLE test_keyspace.foo (id text primary key)")
        EntityColumn entityColumn = new EntityColumn(name: "column_name_", type: "text")
        cql.createColumn(entityColumn, "foo", "test_keyspace")
        assert "column_name_" == describe.getSchemaColumn("test_keyspace", "foo", "column_name_").columnName
        connector.session.execute("DROP TABLE test_keyspace.foo")
    }
}
