package com.fooock.robotstxt.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 *
 */
@Slf4j
@EnableCaching
@Configuration
public class CacheConfiguration {
    @Value("${cache.entry.ttl}")
    private int entryTtl;

    @Bean
    public RedisCacheManager cacheManager(@Qualifier("connectionFactory") RedisConnectionFactory connection) {
        log.debug("Cache TTL is {} minutes", entryTtl);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(entryTtl))
                .disableCachingNullValues();
        return RedisCacheManager.builder(connection).cacheDefaults(configuration).build();
    }
}
