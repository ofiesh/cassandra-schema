package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.accessor.system.DescribeAccessor
import com.chowculator.cassandraschema.model.entity.EntityColumn
import com.chowculator.cassandraschema.model.system.SchemaColumn
import com.chowculator.cassandraschema.model.system.SchemaColumnFamily
import com.chowculator.cassandraschema.model.system.SchemaKeyspace
import spock.lang.*

class AutoSchemaTest extends Specification {
    DescribeAccessor describe
    Cql cql
    AutoSchema autoSchema

    def setup() {
        describe = Mock(DescribeAccessor)
        describe.getSchemaKeyspace("exists") >> new SchemaKeyspace(keyspaceName: "exists")
        describe.getSchemaKeyspace("not_exists") >> null
        describe.getSchemaColumnFamily("exists", "table_exists") >>
                new SchemaColumnFamily(columnFamilyName: "table_exists")
        describe.getSchemaColumn("exists", "table_exists", "column_exists") >>
                new SchemaColumn(columnName: "column_exists")

        cql = Mock(Cql)
        autoSchema = new AutoSchema(describe, cql)
    }

    def "create keyspace is never called"() {
        when:
        autoSchema.createIfNotExists("exists")

        then:
        0 * cql.createKeyspace(_)
    }

    def "create keyspace is called"() {
        when:
        autoSchema.createIfNotExists("not_exists")

        then:
        1 * cql.createKeyspace(_)
    }

    def "autoschema should create column"() {
        when:
        autoSchema.generate("exists", TableExists)

        then:
        0 * cql.createTable(_, _)
        1 * cql.createColumn(new EntityColumn(name: "columnDne", type: "text"), _, _)
        0 * cql.createColumn(new EntityColumn(name: "column_exists", type: "text"), _, _)
        0 * cql.createColumn(new EntityColumn(name: "key", type: "text"), _, _)
    }
}
