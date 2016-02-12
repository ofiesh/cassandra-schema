package com.chowculator.cassandraschema.model.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class Entity {
    String name
    LinkedHashSet<EntityColumn> columns
    List<String> partitionKeys
    List<String> clusteringKeys
}
