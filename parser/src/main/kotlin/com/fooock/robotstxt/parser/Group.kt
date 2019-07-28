package com.fooock.robotstxt.parser

/**
 * Group is a block composed with one or more user agents and a list of
 * one or multiple rules. Optionally, one group can contain one directive to
 * represent the crawl delay for the given user agents
 */
class Group {
    val userAgents = mutableSetOf<String>()
    val rules = mutableSetOf<Rule>()

    // When crawl delay is not present, the crawl delay directive doesn't exists
    var delay: Float? = null
}

/**
 * Group to allow access to all resources of the given host
 */
class AllowAllGroup {
    val group = Group()

    init {
        group.rules.add(Rule("allow", "/"))
        group.userAgents.add("*")
    }
}

/**
 * Group to disallow access to all resources of the given host
 */
class DisallowAllGroup {
    val group = Group()

    init {
        group.rules.add(Rule("disallow", "/"))
        group.userAgents.add("*")
    }
}
