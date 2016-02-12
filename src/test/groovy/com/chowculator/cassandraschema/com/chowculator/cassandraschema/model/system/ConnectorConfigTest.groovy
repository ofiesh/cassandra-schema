package com.chowculator.cassandraschema.com.chowculator.cassandraschema.model.system

import com.chowculator.cassandraschema.connection.ConnectorConfig
import org.junit.Test

class ConnectorConfigTest {
    @Test
    void testWithoutKeyspace() {
        ConnectorConfig config = new ConnectorConfig()
        config.host = "1"
        config.port = 2
        config.keyspace = 3
        ConnectorConfig configWithoutKeyspace = config.clone()
        configWithoutKeyspace.keyspace = null
        assert configWithoutKeyspace == config.withoutKeyspace()
    }
}
