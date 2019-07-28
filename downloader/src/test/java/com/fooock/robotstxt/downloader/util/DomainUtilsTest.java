package com.fooock.robotstxt.downloader.util;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DomainUtilsTest {

    @Test
    public void testExtractDomain() throws Exception {
        assertEquals("http://abc.com", DomainUtils.base(new URI("http://abc.com/test/dir/1.html")));
        assertEquals("http://abc.com:8080", DomainUtils.base(new URI("http://abc.com:8080/test/dir/1.html")));

        assertEquals("http://test.abc.com", DomainUtils.base(new URI("http://test.abc.com/test/dir/1.html#Download")));
        assertEquals("http://test.abc.com:8080", DomainUtils.base(new URI("http://test.abc.com:8080/test/dir/1.html#1")));

        assertEquals("http://sec.test.abc.com", DomainUtils.base(new URI("http://sec.test.abc.com/test/dir/1.html#Download")));
        assertEquals("http://sec.test.abc.com:8080", DomainUtils.base(new URI("http://sec.test.abc.com:8080/test/dir/1.html#1")));
    }
}
