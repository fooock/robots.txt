package com.fooock.robotstxt.database

import com.fooock.robotstxt.database.entity.Entry
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 * Repository to handle robots.txt operations
 */
@Repository
interface RobotsRepository : PagingAndSortingRepository<Entry, Long> {
    /**
     * Search in the database URLs that needs to be updated, that is, current_date - updated_at > entry.age.
     * These URLs need to be re-downloaded to check if a new rule was added.
     */
    @Query(
        "SELECT e.host FROM entries e WHERE cast(now() as date) - cast(e.updated_at as date) > e.age",
        nativeQuery = true
    )
    fun findNeedToUpdateUrls(): List<String>

    /**
     * Search in the database the given URL and returns its current rules
     */
    fun findByHost(host: String): Entry
}
