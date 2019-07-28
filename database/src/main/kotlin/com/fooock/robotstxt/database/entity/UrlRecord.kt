package com.fooock.robotstxt.database.entity

import java.util.*

/**
 *
 */
class UrlRecord(private val url: String) {

    companion object {
        const val KEY_URL = "url"
        const val KEY_PRIORITY = "priority"
    }

    private val priority: String = "9"

    /**
     * @return [Map] to store class values
     */
    fun toMap(): Map<String, String> {
        val content = HashMap<String, String>()
        content[KEY_URL] = url
        content[KEY_PRIORITY] = priority
        return content
    }
}
