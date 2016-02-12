package com.chowculator.cassandraschema.model.entity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class EntityColumn {
    String name
    String type
}
