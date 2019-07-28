package com.fooock.robotstxt.downloader.util;

import java.net.URI;

/**
 *
 */
public class DomainUtils {

    /**
     * Extract domain name without path and query parts. It includes current
     * protocol and port if there is one.
     *
     * @param uri Unknown URL
     * @return url domain
     */
    public static String base(URI uri) {
        return uri.getPort() < 0
                ? String.format("%s://%s", uri.getScheme(), uri.getHost())
                : String.format("%s://%s:%s", uri.getScheme(), uri.getHost(), uri.getPort());
    }
}
