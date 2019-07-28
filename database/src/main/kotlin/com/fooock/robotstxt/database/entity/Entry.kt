package com.fooock.robotstxt.database.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.*

/**
 * Represent the rules of the given host
 */
@EntityListeners(AuditingEntityListener::class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
@Table(name = "entries")
@Entity
class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    lateinit var host: String

    // Default entry age. See issue #5 for more details on how to implement a more advanced strategy
    // to update this value. This value is represented in hours
    val age: Int = 24

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    lateinit var rules: String

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: Date

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: Date

    /**
     * Check if this entry needs to be updated based in its current [updatedAt] field.
     * Normally, an entry is updated if the elapsed time between the current date and the entry date
     * is grater than one day (the default entry age).
     *
     * @return True if need to be updated, false otherwise.
     */
    fun needsUpdate(): Boolean {
        val updatedLocalDate = Instant.ofEpochMilli(updatedAt.time)
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime()

        val now = LocalDateTime.now(ZoneOffset.UTC)
        val hours = ChronoUnit.HOURS.between(updatedLocalDate, now)

        // TODO: Parametrize this field? see #5
        return hours > age
    }
}
