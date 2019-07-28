package com.fooock.robotstxt.database

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

/**
 * Handle redis basic operations under URLs
 */
@Repository
class RedisUrlRepository(@Qualifier("redisTemplate") private val redis: StringRedisTemplate) {
    private var operations: ValueOperations<String, String>? = null

    @PostConstruct
    private fun initialize() {
        operations = redis.opsForValue()
    }

    /**
     * Mark a URL as pending, this is, to be downloaded or downloading. When the given timeout expires, the URL
     * is automatically removed.
     */
    fun addPending(url: String, timeout: Long, unit: TimeUnit): Boolean {
        return operations?.setIfAbsent(url, "pending", timeout, unit)!!
    }

    /**
     * Check if the given [url] is pending to be downloaded, downloading or downloaded and the
     * time has not expired yet.
     *
     * @return True if pending, false otherwise
     */
    fun exists(url: String): Boolean {
        return operations?.get(url) != null
    }

    /**
     * Delete a URL from the queue
     */
    fun deletePending(url: String) {
        redis.delete(url)
    }
}
