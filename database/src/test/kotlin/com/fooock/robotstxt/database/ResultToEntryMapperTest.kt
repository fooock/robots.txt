package com.fooock.robotstxt.database

import com.fooock.robotstxt.database.mapper.ResultToEntryMapper
import com.fooock.robotstxt.parser.AllowAllGroup
import com.fooock.robotstxt.parser.Result
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 *
 */
class ResultToEntryMapperTest {
    @Test
    fun `transform Result to Entry`() {
        val allowResult = AllowAllGroup().group

        val result = Result.Builder()
            .groups(setOf(allowResult))
            .build()

        val mapper = ResultToEntryMapper()
        val entry = mapper.map("https://example.com", result)

        assertThat(entry.host).isEqualTo("https://example.com")
        assertThat(entry.rules).isEqualTo("{\"sitemaps\":[],\"groups\":[{\"userAgents\":[\"*\"],\"rules\":[{\"type\":\"allow\",\"value\":\"/\"}]}]}")
    }
}
