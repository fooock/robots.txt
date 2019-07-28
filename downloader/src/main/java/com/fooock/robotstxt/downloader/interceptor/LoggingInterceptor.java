package com.fooock.robotstxt.downloader.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
@Slf4j
public class LoggingInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.currentTimeMillis();
        Response resp = chain.proceed(request);

        long t2 = System.currentTimeMillis();
        log.info("Received response (code={}) for {} in {}ms", resp.code(), resp.request().url(), (t2 - t1));

        return resp;
    }
}
