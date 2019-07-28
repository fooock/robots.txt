package com.fooock.robotstxt.downloader;

import com.fooock.robotstxt.downloader.handler.ParserResultListener;
import com.fooock.robotstxt.downloader.interceptor.LoggingInterceptor;
import com.fooock.robotstxt.downloader.interceptor.ResponseErrorInterceptor;
import com.fooock.robotstxt.parser.BaseParser;
import com.fooock.robotstxt.parser.DefaultRobotParser;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@EntityScan("com.fooock.robotstxt")
@EnableJpaRepositories("com.fooock.robotstxt.database")
@ComponentScan("com.fooock.robotstxt")
@SpringBootApplication
public class DownloaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DownloaderApplication.class, args);
    }

    @Bean
    public BaseParser robotsParser() {
        return new DefaultRobotParser();
    }

    @Bean
    public OkHttpClient okHttpClient(ParserResultListener resultListener) {
        return new OkHttpClient.Builder()
                .addInterceptor(new ResponseErrorInterceptor(resultListener))
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }
}
