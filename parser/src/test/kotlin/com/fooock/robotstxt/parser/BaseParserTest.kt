package com.fooock.robotstxt.parser

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.stream.Collectors

/**
 *
 */
class BaseParserTest {
    private fun readFile(path: String): ByteArray = this::class.java.classLoader.getResource(path)!!.readBytes()

    @Test
    fun `parse basic robots with sitemap`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("basic-robots.txt"))

        // Test sitemap
        assertThat(result).isNotNull
        assertThat(result.sitemaps.size).isEqualTo(1)
        assertThat(result.sitemaps.elementAt(0)).isEqualTo("https://example.com/sitemap.xml")

        // Test groups
        assertThat(result.groups.size).isEqualTo(2)
        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("googlebot", "*"))
    }

    @Test
    fun `parse basic robots file without sitemap`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("basic-robots-without-sitemap.txt"))

        // Test sitemap
        assertThat(result).isNotNull
        assertThat(result.sitemaps.size).isEqualTo(0)

        // Test groups
        assertThat(result.groups.size).isEqualTo(1)
        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("*"))

        // Test rules
        val group = result.groups.elementAt(0)
        assertThat(group.rules.size).isEqualTo(3)
        assertThat(group.rules.elementAt(0).type).isEqualTo("allow")
        assertThat(group.rules.elementAt(1).type).isEqualTo("disallow")
        assertThat(group.rules.elementAt(2).type).isEqualTo("disallow")

        val rules: List<String> = group.rules.map { it.value }
        assertThat(rules).isEqualTo(listOf("/", "/test/admin.html", "/test/budget.php"))
    }

    @Test
    fun `parse group with multiple agents`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("group-with-multiple-agents.txt"))

        // Test groups
        assertThat(result.groups.size).isEqualTo(1)
        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("a", "b", "c", "d"))

        // Test rules
        val group = result.groups.elementAt(0)
        assertThat(group.rules.size).isEqualTo(1)
        assertThat(group.rules.elementAt(0).type).isEqualTo("allow")

        val rules: List<String> = group.rules.map { it.value }
        assertThat(rules).isEqualTo(listOf("/"))
    }

    @Test
    fun `parse crawl delay`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("crawl-delay-robots.txt"))

        // Test groups
        assertThat(result.groups.size).isEqualTo(3)
        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("*", "cybermapper", "awesomebot"))

        // Test rules
        val group1 = result.groups.elementAt(0)
        assertThat(group1.rules.size).isEqualTo(1)
        assertThat(group1.delay).isEqualTo(1f)
        assertThat(group1.rules.elementAt(0).type).isEqualTo("disallow")

        val group2 = result.groups.elementAt(1)
        assertThat(group2.rules.size).isEqualTo(1)
        assertThat(group2.delay).isEqualTo(null)
        assertThat(group2.rules.elementAt(0).type).isEqualTo("disallow")

        val group3 = result.groups.elementAt(2)
        assertThat(group3.rules.size).isEqualTo(1)
        assertThat(group3.delay).isEqualTo(0.7f)
        assertThat(group3.rules.elementAt(0).type).isEqualTo("allow")
    }

    @Test
    fun `parse robots with multiple disallow`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("multiple-disallow.txt"))

        // Test groups
        assertThat(result.groups.size).isEqualTo(1)
        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("figtree"))

        // Test rules
        val group = result.groups.elementAt(0)
        assertThat(group.delay).isEqualTo(3f)
        assertThat(group.rules.size).isEqualTo(4)
        group.rules.forEach { rule -> assertThat(rule.type).isEqualTo("disallow") }

        val rules: List<String> = group.rules.map { it.value }
        assertThat(rules).isEqualTo(listOf("/tmp", "/a%3cd.html", "/a%2fb.html", "/%7ejoe/index.html"))
    }

    @Test
    fun `parse malformed robots file`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("malformed-robots.txt"))

        assertThat(result.groups.size).isEqualTo(2)
        assertThat(result.sitemaps.size).isEqualTo(1)
        assertThat(result.sitemaps.elementAt(0)).isEqualTo("/sitemap.xml")

        // Test first group
        val group1 = result.groups.elementAt(0)
        assertThat(group1.rules.size).isEqualTo(2)
        val keys1: List<String> = group1.rules.map { it.type }
        val rules1: List<String> = group1.rules.map { it.value }
        assertThat(keys1).isEqualTo(listOf("disallow", "disallow"))
        assertThat(rules1).isEqualTo(listOf("/whitespace-before-colon/", "/no-colon-useragent/"))

        // Test second group
        val group2 = result.groups.elementAt(1)
        assertThat(group2.rules.size).isEqualTo(2)
        val keys2: List<String> = group2.rules.map { it.type }
        val rules2: List<String> = group2.rules.map { it.value }
        assertThat(keys2).isEqualTo(listOf("allow", "disallow"))
        assertThat(rules2).isEqualTo(listOf("/whitespace-before-colon", "/"))
    }

    @Test
    fun `parse robots with typos`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("typos-robots.txt"))

        assertThat(result.groups.size).isEqualTo(1)
        assertThat(result.sitemaps.size).isEqualTo(0)

        // Test group
        val group = result.groups.elementAt(0)
        assertThat(group.userAgents.size).isEqualTo(1)
        assertThat(group.userAgents.elementAt(0)).isEqualTo("bot1")

        assertThat(group.rules.size).isEqualTo(3)
        val keys: List<String> = group.rules.map { it.type }
        assertThat(keys).isEqualTo(listOf("disallow", "disallow", "disallow"))
        val rules: List<String> = group.rules.map { it.value }
        assertThat(rules).isEqualTo(listOf("/useragent/", "/useg-agent/", "/useragent-no-colon/"))
    }

    @Test
    fun `parse dzone robots txt file`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("dzone-robots.txt"))

        assertThat(result.groups.size).isEqualTo(8)
        assertThat(result.sitemaps.size).isEqualTo(1)
    }

    @Test
    fun `parse utf8 BOM robots txt file`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("robots-utf-8-bom.txt"))

        assertThat(result.groups.size).isEqualTo(2)
        assertThat(result.sitemaps.size).isEqualTo(1)

        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("*", "googlebot"))
    }

    @Test
    fun `parse utf16 big endian BOM robots txt file`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("robots-utf16be-bom.txt"))

        assertThat(result.groups.size).isEqualTo(2)
        assertThat(result.sitemaps.size).isEqualTo(1)

        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("*", "googlebot"))
    }

    @Test
    fun `parse utf16 little endian BOM robots txt file`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("robots-utf16le-bom.txt"))

        assertThat(result.groups.size).isEqualTo(2)
        assertThat(result.sitemaps.size).isEqualTo(1)

        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("*", "googlebot"))
    }

    @Test
    fun `no content allow all`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", ByteArray(0))

        assertThat(result.groups.size).isEqualTo(1)
        assertThat(result.sitemaps.size).isEqualTo(0)

        val group = result.groups.elementAt(0)
        assertThat(group.rules.size).isEqualTo(1)
        assertThat(group.rules.elementAt(0).type).isEqualTo("allow")

        val rules: List<String> = group.rules.map { it.value }
        assertThat(rules).isEqualTo(listOf("/"))
    }

    @Test
    fun `parse stack overflow robots txt file`() {
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("stackoverflow-robots.txt"))

        assertThat(result.groups.size).isEqualTo(6)
        assertThat(result.sitemaps.size).isEqualTo(1)

        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("*", "mediapartners-google", "yahoo", "008", "voltron", "googlebot-image"))
        assertThat(result.sitemaps.elementAt(0)).isEqualTo("https://stackoverflow.com/sitemap.xml")

        assertThat(result.groups.elementAt(0).rules.size).isEqualTo(67)
        assertThat(result.groups.elementAt(1).rules.size).isEqualTo(1)
        assertThat(result.groups.elementAt(2).rules.size).isEqualTo(1)
        assertThat(result.groups.elementAt(3).rules.size).isEqualTo(1)
        assertThat(result.groups.elementAt(4).rules.size).isEqualTo(1)
        assertThat(result.groups.elementAt(5).rules.size).isEqualTo(6)
    }

    @Test
    fun `parse user agent with spaces`() {
        // is not allowed to have spaces in user agents
        val parser = DefaultRobotParser()
        val result = parser.parse("https://example.com", readFile("user-agent-with-spaces.txt"))

        assertThat(result.groups.size).isEqualTo(1)
        assertThat(result.sitemaps.size).isEqualTo(0)

        val names: List<String> =
            result.groups.stream().flatMap { g -> g.userAgents.stream() }.collect(Collectors.toList())
        assertThat(names).isEqualTo(listOf("abc"))
    }
}
