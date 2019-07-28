package com.fooock.robotstxt.parser

/**
 * This class represents a parser [Result]
 */
class Result(val sitemaps: Set<String>, val groups: Set<Group>) {
    /**
     * Builder class to generate new [Result]s
     */
    data class Builder(
        var sitemaps: Set<String> = emptySet(),
        var groups: Set<Group> = emptySet()
    ) {
        fun sitemap(sitemaps: Set<String>) = apply { this.sitemaps = sitemaps }
        fun groups(groups: Set<Group>) = apply { this.groups = groups }
        fun build() = Result(sitemaps, groups)
    }
}
