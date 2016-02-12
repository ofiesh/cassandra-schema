package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.Entity
import com.chowculator.cassandraschema.model.entity.EntityColumn

import static org.junit.Assert.assertEquals

import org.junit.Test

class CreateEntityTableTest {
    def static normalize(String str) {
        str.replaceAll("\n", "").replaceAll("\\s+", " ")
    }

    @Test
    public void entityToCqlShouldCreateTable() {
        println new File(".").getAbsolutePath()
        Entity entity = new Entity();
        entity.name = "table_name"
        entity.columns = new LinkedHashSet<>()
        entity.columns.addAll([
                new EntityColumn(name: "p_k", type: "int"),
                new EntityColumn(name: "p_k1", type: "text"),
                new EntityColumn(name: "c_k", type: "text"),
                new EntityColumn(name: "c_k1", type: "int"),
                new EntityColumn(name: "normal", type: "int"),
                new EntityColumn(name: "dec_imal", type: "decimal")])
        entity.partitionKeys = ["p_k", "p_k1"]
        entity.clusteringKeys = ["c_k", "c_k1"]
        def expected = new File("src/test/resources/fixtures/entity_table.cql").text
        def normalizedExpected = normalize(expected)
        def normalizedEntityCql = normalize(CreateEntityTable.toCql(entity, "keyspace_test"))
        assertEquals(normalizedExpected, normalizedEntityCql)

    }
}
