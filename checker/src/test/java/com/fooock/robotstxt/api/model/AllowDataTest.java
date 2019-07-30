package com.fooock.robotstxt.api.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class AllowDataTest {

    @Test
    public void testAgent() {
        AllowData d1 = new AllowData();
        d1.setAgent(null);
        assertFalse(d1.isAgentValid());

        AllowData d2 = new AllowData();
        d2.setAgent("");
        assertFalse(d2.isAgentValid());

        AllowData d3 = new AllowData();
        d3.setAgent("AwesomeBot");
        assertTrue(d3.isAgentValid());
    }
}
