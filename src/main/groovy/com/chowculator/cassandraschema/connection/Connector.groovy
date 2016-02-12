package com.chowculator.cassandraschema.connection

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session

class Connector {
    final Session session
    Connector(ConnectorConfig config) {
        def cluster = Cluster.builder().addContactPoint(config.host).withPort(config.port).build()
        session = cluster.connect(config.keyspace)
    }
}
