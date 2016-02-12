package com.chowculator.cassandraschema.autoschema

import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table


@Table(name = "table_dne")
class TableDne {
    @PartitionKey
    String foo
}
