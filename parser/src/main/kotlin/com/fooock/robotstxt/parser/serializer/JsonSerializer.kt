package com.fooock.robotstxt.parser.serializer

import com.fooock.robotstxt.parser.Result
import com.google.gson.Gson

/**
 * Serialize a [Result] object to JSON
 */
class JsonSerializer : BaseSerializer<Result, String> {
    private val gson = Gson()
    override fun serialize(obj: Result): String = gson.toJson(obj)
}
