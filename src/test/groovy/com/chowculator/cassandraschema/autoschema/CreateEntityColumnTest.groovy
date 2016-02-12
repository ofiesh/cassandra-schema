package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.EntityColumn
import org.junit.Test

class CreateEntityColumnTest {
    @Test
    public void EntityColumnShouldCreateAlter() {
        def expected = "ALTER TABLE test_keyspace.test_table ADD foo text"
        EntityColumn entityColumn = new EntityColumn(name: "foo", type: "text")
        def actual = CreateEntityColumn.toCql(entityColumn, "test_table", "test_keyspace")
        assert expected == actual.toString()
    }
}
