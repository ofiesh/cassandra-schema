package com.chowculator.cassandraschema.model.system

import com.datastax.driver.mapping.annotations.ClusteringColumn
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table

@Table(name = "schema_columns")
class SchemaColumn {
    @PartitionKey
    @Column (name = "keyspace_name")
    String keyspaceName

    @ClusteringColumn(0)
    @Column (name = "columnfamily_name")
    String columnFamilyName

    @ClusteringColumn(1)
    @Column (name = "column_name")
    String columnName
}
