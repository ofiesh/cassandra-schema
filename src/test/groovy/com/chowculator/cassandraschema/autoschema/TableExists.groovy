package com.chowculator.cassandraschema.autoschema

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table

@Table(name = "table_exists")
class TableExists {
    @PartitionKey
    String key

    @Column(name = "column_exists")
    String columnExists

    String columnDne
}
