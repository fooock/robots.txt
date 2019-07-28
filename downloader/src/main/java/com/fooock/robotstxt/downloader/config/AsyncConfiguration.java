package com.fooock.robotstxt.downloader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 *
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
    @Value("${async.core.size}")
    private int corePoolSize;

    @Value("${async.queue.size}")
    private int queueSize;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueSize);
        executor.setThreadNamePrefix("Downloader-");
        executor.initialize();
        return executor;
    }
}
