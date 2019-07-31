package com.fooock.robotstxt.parser.serializer

import com.fooock.robotstxt.parser.DefaultRobotParser

/**
 *
 */
class GenerateSerializerJson {
    fun readFile(path: String): ByteArray = this::class.java.classLoader.getResource(path)!!.readBytes()
}

fun main(args: Array<String>) {
    val gen = GenerateSerializerJson()
    val parser = DefaultRobotParser()

    val result = parser.parse("https://example.com", gen.readFile(args[0]))
    val jsonSerializer = JsonSerializer()
    val json = jsonSerializer.serialize(result)

    print(json)
}
