package com.fooock.robotstxt.downloader.handler;

import com.fooock.robotstxt.downloader.type.ResultType;
import okhttp3.Headers;

/**
 *
 */
public interface ParserResultListener {
    /**
     * Method called when a new parser result is created
     *
     * @param url    Current host used to extract robots rules
     * @param result Parser result containing rules
     */
    void onResult(String url, Headers headers, byte[] result, ResultType type);
}
