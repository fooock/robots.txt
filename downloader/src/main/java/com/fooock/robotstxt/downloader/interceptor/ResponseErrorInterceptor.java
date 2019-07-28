package com.fooock.robotstxt.downloader.interceptor;

import com.fooock.robotstxt.downloader.handler.ParserResultListener;
import com.fooock.robotstxt.downloader.type.ResultType;
import com.fooock.robotstxt.downloader.util.DomainUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
public class ResponseErrorInterceptor implements Interceptor {
    private final ParserResultListener resultListener;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        log.debug("Response from {} is {}", response.request().url(), response.code());

        if (response.isSuccessful()) return response;

        // Check if response is 4xx or 5xx error codes
        if (is4xxErrorCode(response.code())) {
            log.warn("Response from {} is 4xx error code", response.request().url());
            notifyResult(response, ResultType.ALLOW_ALL);
        }
        if (is5xxErrorCode(response.code())) {
            log.warn("Response from {} is 5xx error code", response.request().url());
            notifyResult(response, ResultType.DISABLE_ALL);
        }
        return response;
    }

    /**
     * Send the result to the listener
     */
    private void notifyResult(Response response, ResultType allowAll) {
        resultListener.onResult(DomainUtils.base(response.request().url().uri()), response.headers(), new byte[0], allowAll);
    }

    /**
     * @return True if code is a 4xx error, false otherwise
     */
    private boolean is4xxErrorCode(int code) {
        return code < 500 && code >= 400;
    }

    /**
     * @return True if code is a 5xx error, false otherwise
     */
    private boolean is5xxErrorCode(int code) {
        return code < 600 && code >= 500;
    }
}
