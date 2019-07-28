package com.fooock.robotstxt.database.mapper

import com.fooock.robotstxt.database.entity.Entry

/**
 * Base interface to transform data in this project
 */
interface EntryMapper<T> {
    fun map(host: String, type: T): Entry
}
