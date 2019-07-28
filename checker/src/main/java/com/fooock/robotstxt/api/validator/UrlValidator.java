package com.fooock.robotstxt.api.validator;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * URL validator utilities
 */
public final class UrlValidator {

    /**
     * Check if the given URL is valid or not
     *
     * @param url URL to be checked
     * @return True i valid, false otherwise
     */
    public static boolean isValid(String url) {
        if (url == null || url.isEmpty()) return false;
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Create an {@link URI} based on the current url
     *
     * @param url URL to transform to URI
     * @return URI or null if an error occurs
     */
    public static URI toUri(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            return null;
        }
    }

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
