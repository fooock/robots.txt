package com.fooock.robotstxt.parser

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 *
 */
class AllowAllGroupTest {
    @Test
    fun `allow all group`() {
        val all = AllowAllGroup()
        assertEquals(1, all.group.userAgents.size)
        assertEquals(1, all.group.rules.size)

        assertEquals("*", all.group.userAgents.elementAt(0))
        assertEquals("/", all.group.rules.elementAt(0).value)
        assertEquals("allow", all.group.rules.elementAt(0).type)
    }
}
