package com.fooock.robotstxt.downloader.config;

import com.fooock.robotstxt.downloader.handler.ParserResultListener;
import com.fooock.robotstxt.downloader.interceptor.CrawlerNameHeaderInterceptor;
import com.fooock.robotstxt.downloader.interceptor.LoggingInterceptor;
import com.fooock.robotstxt.downloader.interceptor.ResponseErrorInterceptor;
import com.fooock.robotstxt.parser.BaseParser;
import com.fooock.robotstxt.parser.DefaultRobotParser;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.TimeUnit;

/**
 * Application configuration
 */
@EntityScan("com.fooock.robotstxt")
@EnableJpaRepositories("com.fooock.robotstxt.database")
@ComponentScan("com.fooock.robotstxt")
@Configuration
public class AppConfiguration {

    /**
     * Creates a custom http client to make network requests to download
     * robots.txt files. This client follow all redirects.
     *
     * @param resultListener Parser result listener
     * @return Custom http client
     */
    @Bean
    public OkHttpClient okHttpClient(ParserResultListener resultListener) {
        return new OkHttpClient.Builder()
                .addInterceptor(new CrawlerNameHeaderInterceptor())
                .addInterceptor(new ResponseErrorInterceptor(resultListener))
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    @Bean
    public BaseParser robotsParser() {
        return new DefaultRobotParser();
    }
}
