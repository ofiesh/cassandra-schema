package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.Entity
import com.chowculator.cassandraschema.model.entity.EntityColumn
import spock.lang.Specification

import static org.junit.Assert.assertEquals

import org.junit.Test

class CreateEntityTableTest extends Specification {
    def static normalize(String str) {
        str.replaceAll("\n", " ").replaceAll("\\s+", " ").trim()
    }

    def "entity with clustering key should match cql"() {
        setup:
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

        when:
        def actual = CreateEntityTable.toCql(entity, "keyspace_test")

        then:
        normalize(actual) == normalize("""
CREATE TABLE keyspace_test.table_name (
    p_k int,
    p_k1 text,
    c_k text,
    c_k1 int,
    normal int,
    dec_imal decimal,
    PRIMARY KEY ((p_k, p_k1), c_k, c_k1)
)""")
    }


    def "entity without clustering key should match cql"() {
        setup:
        Entity entity = new Entity();
        entity.name = "table_name"
        entity.columns = new LinkedHashSet<>()
        entity.columns.addAll([
                new EntityColumn(name: "p_k", type: "int"),
                new EntityColumn(name: "p_k1", type: "text"),
                new EntityColumn(name: "normal", type: "int"),
                new EntityColumn(name: "dec_imal", type: "decimal")])
        entity.partitionKeys = ["p_k", "p_k1"]

        when:
        def actual = CreateEntityTable.toCql(entity, "keyspace_test")

        then:
        normalize(actual) == normalize("""
CREATE TABLE keyspace_test.table_name (
    p_k int,
    p_k1 text,
    normal int,
    dec_imal decimal,
    PRIMARY KEY ((p_k, p_k1))
)""")
    }
}
