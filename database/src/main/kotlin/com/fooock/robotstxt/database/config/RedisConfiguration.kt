package com.fooock.robotstxt.database.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

/**
 *
 */
@EnableRedisRepositories
@PropertySource("classpath:redis.properties")
@Configuration
class RedisConfiguration(private val env: Environment) {

    @Bean
    fun connectionFactory(): RedisConnectionFactory {
        val config = RedisClusterConfiguration()
        config.clusterNode(env.getProperty("redis.host")!!, env.getProperty("redis.port")!!.toInt())
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(): StringRedisTemplate = StringRedisTemplate(connectionFactory())
}
