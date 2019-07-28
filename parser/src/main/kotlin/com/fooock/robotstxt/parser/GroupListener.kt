package com.fooock.robotstxt.parser

import com.fooock.robotstxt.parser.generated.RobotsTxtBaseListener
import com.fooock.robotstxt.parser.generated.RobotsTxtParser

/**
 *
 */
class GroupListener : RobotsTxtBaseListener() {
    private var group: Group? = null

    val sitemaps = mutableSetOf<String>()
    val groups = mutableSetOf<Group>()

    override fun enterGroup(ctx: RobotsTxtParser.GroupContext?) {
        group = Group()
    }

    override fun exitSitemapRule(ctx: RobotsTxtParser.SitemapRuleContext?) {
        val url = ctx?.URL()
        // If url is not null then add it to the current list of valid
        // siteMap and return. If null, then get current value because
        // it can be a relative path.
        url?.let { sitemaps.add(it.text); return }

        // TODO: use the current host to create and absolute path to the
        // sitemap.xml file using this relative path
        val path = ctx?.ID()
        path?.let { sitemaps.add(it.text) }
    }

    override fun exitAgentRule(ctx: RobotsTxtParser.AgentRuleContext?) {
        // Add user agent name for the current group
        val texts = ctx?.agentName()?.children
        if (texts?.isEmpty()!!) return

        // If user agent directive contains multiple strings separated by space,
        // then we only save as valid user agent the first one, because as the
        // draft standard, user agent only can contain a-zA-Z_-
        group?.userAgents?.add(texts[0].text.toLowerCase())
    }

    override fun exitGroup(ctx: RobotsTxtParser.GroupContext?) {
        // First we need to check if the current group has values or not. If no values found
        // in this exit group, we don't add noting
        if (group?.userAgents?.isEmpty()!!) return

        // Add created group to the list of current robots.txt groups
        // Note that a only a group can contain rules
        group?.let { groups.add(it) }
        group = null
    }

    override fun exitAllowRule(ctx: RobotsTxtParser.AllowRuleContext?) {
        val key = ctx?.ALLOW()?.text?.toLowerCase()
        val value = if (ctx?.ID().isNullOrEmpty()) "" else ctx?.ID()?.first()?.text

        val rule = Rule(key!!, value!!)
        group?.rules?.add(rule)
    }

    override fun exitDisallowRule(ctx: RobotsTxtParser.DisallowRuleContext?) {
        val key = ctx?.DISALLOW()?.text?.toLowerCase()
        val value = if (ctx?.ID().isNullOrEmpty()) "" else ctx?.ID()?.first()?.text

        val rule = Rule(key!!, value!!)
        group?.rules?.add(rule)
    }

    override fun exitCrawlDelayRule(ctx: RobotsTxtParser.CrawlDelayRuleContext?) {
        try {
            val delay = ctx?.NUMBER()?.text?.toFloat()
            group?.delay = delay
        } catch (e: NumberFormatException) {
            // If an exception occurs, then remove value
            group?.delay = null
        }
    }
}
