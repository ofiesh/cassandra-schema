package com.chowculator.cassandraschema.accessor.system

import com.chowculator.cassandraschema.model.system.SchemaColumn
import com.chowculator.cassandraschema.model.system.SchemaColumnFamily
import com.chowculator.cassandraschema.model.system.SchemaKeyspace
import com.datastax.driver.mapping.Result
import com.datastax.driver.mapping.annotations.Accessor
import com.datastax.driver.mapping.annotations.Param
import com.datastax.driver.mapping.annotations.Query

@Accessor
interface DescribeAccessor {
    @Query("SELECT keyspace_name FROM system.schema_keyspaces WHERE keyspace_name = :name")
    SchemaKeyspace getSchemaKeyspace(@Param("name") String keyspaceName)

    @Query("SELECT keyspace_name, columnfamily_name FROM system.schema_columnfamilies WHERE keyspace_name = :name AND columnfamily_name = :columnFamilyName")
    SchemaColumnFamily getSchemaColumnFamily(@Param("name") String keyspaceName, @Param("columnFamilyName") String columnFamilyName)

    @Query("SELECT keyspace_name, columnfamily_name FROM system.schema_columnfamilies WHERE keyspace_name = :name")
    Result<SchemaColumnFamily> getSchemaColumnFamilies(@Param("name") String keyspaceName)

    @Query("""SELECT keyspace_name, columnfamily_name, column_name FROM system.schema_columns
              WHERE keyspace_name = :keyspaceName AND columnfamily_name = :columnFamilyName AND column_name = :columnName""")
    SchemaColumn getSchemaColumn(@Param("keyspaceName") String keyspaceName,
                                         @Param("columnFamilyName") String columnFamilyName,
                                         @Param("columnName") String columnName)

    @Query("""SELECT keyspace_name, columnfamily_name, column_name FROM system.schema_columns
              WHERE keyspace_name = :keyspaceName AND columnfamily_name = :columnFamilyName""")
    Result<SchemaColumn> getSchemaColumns(@Param("keyspaceName") String keyspaceName,
                                          @Param("columnFamilyName") String columnFamilyName)
}
