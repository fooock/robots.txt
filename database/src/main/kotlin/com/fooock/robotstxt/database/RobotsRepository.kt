package com.fooock.robotstxt.database

import com.fooock.robotstxt.database.entity.Entry
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Repository to handle robots.txt operations
 */
@Repository
interface RobotsRepository : PagingAndSortingRepository<Entry, Long> {
    /**
     * Search in the database URLs that needs to be updated, that is, current_date - updated_at > entry.age (in days).
     * These URLs need to be re-downloaded to check if a new rule was added / removed.
     */
    @Query(
        "SELECT e.host FROM entries e WHERE cast(now() as date) - cast(e.updated_at as date) > ceil(e.age / 24) LIMIT :size",
        nativeQuery = true
    )
    fun findNeedToUpdateUrls(@Param("size") size: Int): List<String>

    /**
     * Search in the database the given URL and returns its current rules
     */
    fun findByHost(host: String): Entry
}
