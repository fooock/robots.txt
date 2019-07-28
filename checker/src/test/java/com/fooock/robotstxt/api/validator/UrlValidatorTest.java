package com.fooock.robotstxt.api.validator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class UrlValidatorTest {

    @Test
    public void testCheckValidUrls() {
        assertTrue(UrlValidator.isValid("https://google.es"));
        assertTrue(UrlValidator.isValid("http://example.com"));
    }

    @Test
    public void testCheckNotValidUrls() {
        assertFalse(UrlValidator.isValid("abc.hello"));
        assertFalse(UrlValidator.isValid(null));
        assertFalse(UrlValidator.isValid(""));
    }
}
