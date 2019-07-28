package com.fooock.robotstxt.downloader.util;

/**
 *
 */
public class UrlUtils {

    /**
     * Check if a URL starts with {@code http} or {@code https}. If the given
     * URL is null or empty, then this method returns false.
     *
     * @param url Url to check
     * @return True if URL starts with http(s) protocol
     */
    public static boolean isValid(String url) {
        if (url == null || url.isEmpty()) return false;
        url = url.toLowerCase();
        return url.startsWith("http:") || url.startsWith("https:");
    }
}
