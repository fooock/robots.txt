package com.fooock.robotstxt.downloader.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class UrlUtilsTest {
    @Test
    public void testValidUrls() {
        assertTrue(UrlUtils.isValid("htTp://example.com"));
        assertTrue(UrlUtils.isValid("HttpS://example.com"));
    }

    @Test
    public void testNotValidUrls() {
        assertFalse(UrlUtils.isValid("htt://example.com"));
        assertFalse(UrlUtils.isValid("://example.com"));
    }
}
