package com.fooock.robotstxt.database.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.connection.stream.RecordId
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

/**
 *
 */
@Service
class ClusterService(@Qualifier("redisTemplate") private val template: RedisTemplate<String, String>) {
    private val operations = template.opsForStream<String, String>()

    fun add(key: String, content: Map<String, String>): RecordId? = operations.add(key, content)
}
