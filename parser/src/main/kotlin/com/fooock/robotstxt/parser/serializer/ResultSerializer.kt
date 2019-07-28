package com.fooock.robotstxt.parser.serializer

import com.fooock.robotstxt.parser.Result
import com.google.gson.Gson

/**
 * Serialize a JSON string to a valid [Result]
 */
class ResultSerializer : BaseSerializer<String, Result> {
    private val gson = Gson()
    override fun serialize(obj: String): Result = gson.fromJson(obj, Result::class.java)
}
