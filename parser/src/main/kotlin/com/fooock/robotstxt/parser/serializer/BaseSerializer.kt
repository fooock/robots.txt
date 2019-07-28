package com.fooock.robotstxt.parser.serializer

/**
 * Base class for all serializers
 */
interface BaseSerializer<in F, out T> {
    fun serialize(obj: F): T
}
