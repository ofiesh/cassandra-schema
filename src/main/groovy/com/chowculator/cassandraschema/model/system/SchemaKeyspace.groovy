package com.chowculator.cassandraschema.model.system

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table

@Table(name = "system_keyspace")
class SchemaKeyspace {
    @PartitionKey
    @Column (name = "keyspace_name")
    String keyspaceName
}
