package com.chowculator.cassandraschema.connection

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
@AutoClone
class ConnectorConfig {
    String host, keyspace
    Integer port

    ConnectorConfig withoutKeyspace() {
        ConnectorConfig config = new ConnectorConfig()
        config.host = this.host
        config.port = this.port
        config
    }
}
