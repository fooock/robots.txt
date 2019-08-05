package com.fooock.robotstxt.downloader.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *
 */
public class CrawlerNameHeaderInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("User-Agent", "AwesomeRobotsTxt (+https://robotstxt.io)")
                .build();
        return chain.proceed(request);
    }
}
