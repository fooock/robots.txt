package com.fooock.robotstxt.downloader.service;

import com.fooock.robotstxt.downloader.handler.ParserResultListener;
import com.fooock.robotstxt.downloader.type.ResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FileDownloaderService {
    private static final String FIRST_500_KB = "bytes=0-499999";

    private final OkHttpClient httpClient;
    private final ParserResultListener resultListener;

    /**
     * Download the given url to parse robot.txt content. Note that if the network response is
     * any 4xx code, then all resources for the current host can be crawled by any robot.
     *
     * @param baseUrl   Url to be downloaded
     * @param robotsUrl Url pointing to robots.txt resource
     * @see com.fooock.robotstxt.downloader.interceptor.ResponseErrorInterceptor
     */
    @Async
    public void download(String baseUrl, String robotsUrl) {
        log.debug("Make network request to {} (original request domain is {})", robotsUrl, baseUrl);

        Request request = new Request.Builder().get().url(robotsUrl).addHeader("Range", FIRST_500_KB).build();
        try (Response response = httpClient.newCall(request).execute()) {
            // Check response status. Note that if response is not success, the code
            // that handle errors is in the interceptor
            if (!response.isSuccessful()) return;

            // If content type not exists or is not included in text/plain, then we allow all access
            // to this domain. Sometimes robots.txt page redirects to login pages. This behaviour
            // indicate that the robots.txt resource is not available. See section 2.3 of
            // https://tools.ietf.org/html/draft-rep-wg-topic-00
            if (!isValidMediaType(response.header("Content-Type"), baseUrl)) {
                log.info("No valid content type found, allow all from {}", baseUrl);
                resultListener.onResult(baseUrl, response.headers(), new byte[0], ResultType.ALLOW_ALL);
                return;
            }

            ResponseBody body = response.body();
            byte[] result = body == null ? new byte[0] : body.bytes();
            log.debug("Response size from {} is {} bytes", baseUrl, result.length);
            resultListener.onResult(baseUrl, response.headers(), result, ResultType.CUSTOM);

        } catch (Exception e) {
            // Should we do anything if an error occurs?
            // Maybe remove the URL from the queue if the exceptions is not UnknownHostException?
            log.error("Error getting URL {}", robotsUrl, e);
        }
    }

    /**
     * @return True if media type is text/plain, false otherwise
     */
    private boolean isValidMediaType(String type, String url) {
        if (type == null || type.isEmpty()) {
            log.warn("No content type found for url {} ({})", url, type);
            return false;
        }
        MediaType mediaType = MediaType.parseMediaType(type);
        if (mediaType.includes(MediaType.TEXT_PLAIN)) return true;

        log.warn("Content-Type: {} not includes text/plain for url {}", type, url);
        return false;
    }
}
