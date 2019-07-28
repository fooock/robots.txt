package com.fooock.robotstxt.parser.serializer

import com.fooock.robotstxt.parser.Group
import com.fooock.robotstxt.parser.Result
import com.fooock.robotstxt.parser.Rule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 *
 */
class JsonSerializerTest {
    @Test
    fun `from object to json with crawl delay`() {
        val group1 = Group()
        group1.delay = 3f

        group1.userAgents.add("googlebot")
        group1.userAgents.add("yandex")

        group1.rules.add(Rule("allow", "/"))
        group1.rules.add(Rule("disallow", "/admin.php"))

        val result = Result.Builder()
            .sitemap(setOf("https://example.com/sitemap.xml"))
            .groups(setOf(group1))
            .build()

        val jsonSerializer = JsonSerializer()
        val json = jsonSerializer.serialize(result)

        val resultSerializer = ResultSerializer()
        val obj = resultSerializer.serialize(json)

        assertThat(obj.sitemaps.size).isEqualTo(1)
        assertThat(obj.sitemaps.elementAt(0)).isEqualTo("https://example.com/sitemap.xml")
        assertThat(obj.groups.size).isEqualTo(1)
        assertThat(obj.groups.elementAt(0).userAgents).isEqualTo(setOf("googlebot", "yandex"))
        assertThat(obj.groups.elementAt(0).rules.size).isEqualTo(2)
        assertThat(obj.groups.elementAt(0).delay).isEqualTo(3f)
    }

    @Test
    fun `from object to json without crawl delay`() {
        val group1 = Group()

        group1.userAgents.add("googlebot")
        group1.userAgents.add("yandex")

        group1.rules.add(Rule("allow", "/"))
        group1.rules.add(Rule("disallow", "/admin.php"))

        val result = Result.Builder()
            .sitemap(setOf("https://example.com/sitemap.xml"))
            .groups(hashSetOf(group1))
            .build()

        val jsonSerializer = JsonSerializer()
        val json = jsonSerializer.serialize(result)

        val resultSerializer = ResultSerializer()
        val obj = resultSerializer.serialize(json)

        assertThat(obj.sitemaps.size).isEqualTo(1)
        assertThat(obj.sitemaps.elementAt(0)).isEqualTo("https://example.com/sitemap.xml")
        assertThat(obj.groups.size).isEqualTo(1)
        assertThat(obj.groups.elementAt(0).userAgents).isEqualTo(setOf("googlebot", "yandex"))
        assertThat(obj.groups.elementAt(0).rules.size).isEqualTo(2)
        assertThat(obj.groups.elementAt(0).delay).isEqualTo(null)
    }
}
