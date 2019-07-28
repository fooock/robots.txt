package com.fooock.robotstxt.database.mapper

import com.fooock.robotstxt.database.entity.Entry
import com.fooock.robotstxt.parser.Result
import com.fooock.robotstxt.parser.serializer.JsonSerializer

/**
 *
 */
class ResultToEntryMapper : EntryMapper<Result> {
    private val serializer: JsonSerializer = JsonSerializer()

    override fun map(host: String, type: Result): Entry {
        val entry = Entry()
        entry.host = host
        entry.rules = serializer.serialize(type)
        return entry
    }
}
