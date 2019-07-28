package com.fooock.robotstxt.parser

/**
 * Base class to implement robots.txt parsers.
 */
interface BaseParser {

    /**
     * Parse the given [content] of the robots.txt file associated to the [host].
     *
     * @param host The base host of the given robots.txt file
     * @param content Byte array of the robots.txt content
     * @return The parse result
     */
    fun parse(host: String, content: ByteArray): Result
}
