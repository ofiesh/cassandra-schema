package com.chowculator.cassandraschema.autoschema

import com.chowculator.cassandraschema.model.entity.Entity
import com.chowculator.cassandraschema.model.entity.EntityColumn
import com.datastax.driver.mapping.annotations.ClusteringColumn
import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import com.datastax.driver.mapping.annotations.Transient

import java.lang.annotation.Annotation
import java.lang.reflect.Field

class EntityProcessor {
    static Map<Class, String> types = new HashMap<>()
    static {
        types[String] = "text"
        types[Integer] = "int"
        types[int] = "int"
        types[BigDecimal] = "decimal"
    }

    static process(Class klass) {
        Entity entity = new Entity()
        Annotation annotation = klass.getDeclaredAnnotation(Table)
        assert annotation != null : "Class is missing @Table annotation"
        entity.name = annotation.name()

        LinkedHashSet<EntityColumn> columns = new LinkedHashSet<>()
        List<String> partitionKeys = []
        List<String> clusteringKeys = []

        for(MetaProperty prop : klass.metaClass.properties) {
            if(prop.name == "class")
                continue
            Field field = klass.getDeclaredField(prop.name)
            if(field != null && field.getDeclaredAnnotation(Transient) == null) {
                Column column = field.getDeclaredAnnotation(Column)
                String name = column == null ? field.name : column.name()
                String type = types[field.type]
                assert type != null
                columns.add(new EntityColumn(name: name, type: type))

                PartitionKey key = field.getDeclaredAnnotation(PartitionKey)
                if(key != null) {
                    validateIndex(partitionKeys, key.value())
                    partitionKeys[key.value()] = name
                } else {
                    ClusteringColumn col = field.getDeclaredAnnotation(ClusteringColumn)
                    if(col != null) {
                        validateIndex(clusteringKeys, col.value())
                        clusteringKeys[col.value()] = name
                    }
                }
            }
        }

        assert partitionKeys.size() > 0
        partitionKeys.forEach { assert it != null }
        clusteringKeys.forEach { assert it != null }
        entity.clusteringKeys = clusteringKeys
        entity.partitionKeys = partitionKeys
        entity.columns = columns
        entity
    }

    private static validateIndex(List r, int i) {
        assert i >= r.size() || r[i] == null
    }
}
