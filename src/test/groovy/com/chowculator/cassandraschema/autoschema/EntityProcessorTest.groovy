package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.Entity
import com.chowculator.cassandraschema.model.entity.EntityColumn
import com.datastax.driver.mapping.annotations.ClusteringColumn

import static groovy.test.GroovyAssert.shouldFail

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import com.datastax.driver.mapping.annotations.Transient
import org.junit.Test

class EntityProcessorTest {
    @Table(name = "foo_table")
    static class Foo {
        @PartitionKey
        @Column(name = "p_k")
        String pk

        @Transient
        String no

        int bar
    }

    @Table(name = "foo")
    static class Foo1 {
        @PartitionKey(1)
        foo
    }

    @Table(name = "bar")
    static class Bar {
        String bar
    }

    @Table(name = "bar")
    static class Bar1 {
        @PartitionKey
        foo

        @ClusteringColumn(1)
        bar
    }

    @Test
    public void entityShouldProcess() {
        Entity entity = new Entity()
        entity.name = "foo_table"
        entity.columns = new LinkedHashSet<>()
        entity.columns.addAll([new EntityColumn(name: "p_k", type: "text"),
                               new EntityColumn(name: "bar", type: "int")])
        entity.partitionKeys = ["p_k"]
        entity.clusteringKeys = []
        assert entity == EntityProcessor.process(Foo)
    }

    @Test
    public void outOfOrderPartitionKeyShouldFail() {
        shouldFail(AssertionError) {
            EntityProcessor.process(Foo1)
        }
    }

    @Test
    public void missingPartitionKeyShouldFail() {
        shouldFail(AssertionError) {
            EntityProcessor.process(Bar)
        }
    }

    @Test
    public void outOfOrderClusteringColumShouldFail() {
        shouldFail(AssertionError) {
            EntityProcessor.process(Bar1)
        }
    }

    @Table(name = "foo")
    static class DuplicatePartitionKey {
        @PartitionKey
        String a

        @PartitionKey
        String b
    }

    @Test
    public void duplicatePartitionKeyShouldFail() {
        shouldFail(AssertionError) {
            EntityProcessor.process(DuplicatePartitionKey)
        }
    }
}