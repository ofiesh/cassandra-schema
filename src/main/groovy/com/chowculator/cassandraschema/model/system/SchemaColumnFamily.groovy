package com.chowculator.cassandraschema.model.system

import com.datastax.driver.mapping.annotations.ClusteringColumn
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table

@Table(name = "schema_columnfamilies")
class SchemaColumnFamily {
    @PartitionKey
    @Column (name = "keyspace_name")
    String keyspaceName

    @ClusteringColumn
    @Column (name = "columnfamily_name")
    String columnFamilyName
}