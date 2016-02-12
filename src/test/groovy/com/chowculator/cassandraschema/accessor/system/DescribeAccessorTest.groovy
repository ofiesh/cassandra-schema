package com.chowculator.cassandraschema.accessor.system

import com.chowculator.cassandraschema.connection.Connector
import com.chowculator.cassandraschema.connection.ConnectorConfig
import com.datastax.driver.mapping.MappingManager
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class DescribeAccessorTest {
    static Connector connector
    static DescribeAccessor describe

    @BeforeClass
    static void beforeClass() {
        ConnectorConfig connectorConfig = new ConnectorConfig(host: "localhost", port: 9042, keyspace: "system")
        connector = new Connector(connectorConfig)
        describe = new MappingManager(connector.session).createAccessor(DescribeAccessor.class)
    }

    @AfterClass
    static void afterClass() {
        connector.getSession().close()
    }

    @Test
    void getKeyspacesShouldWork() {
        assert describe.getSchemaKeyspace("system").keyspaceName == "system"
    }

    @Test
    void getColumnFamilyShouldWork() {
        assert describe.getSchemaColumnFamily("system", "schema_columnfamilies").columnFamilyName == "schema_columnfamilies"
    }

    @Test
    void getColumnFamiliesShouldWork() {
        assert describe.getSchemaColumnFamilies("system").size() == 20
    }

    @Test
    void getColumnShouldWork() {
        assert describe.getSchemaColumn("system", "schema_columns", "column_name").columnName == "column_name"
    }

    @Test
    void getColumnsShouldWork() {
        assert describe.getSchemaColumns("system", "schema_columns").size() == 9
    }
}
