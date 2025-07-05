package com.kashif.kmmnewsapp.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@Dao
interface HeadlinesDao {
    

    
    @Query("SELECT * FROM headlines WHERE country = :country ORDER BY publishedAt DESC")
    fun getAllHeadlines(country: String = "us"): Flow<List<HeadlineEntity>>

    @Query("SELECT * FROM headlines WHERE country = :country ORDER BY publishedAt DESC")
    suspend fun getAllHeadlinesOnce(country: String = "us"): List<HeadlineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeadlines(headlines: List<HeadlineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeadline(headline: HeadlineEntity)

    @Update
    suspend fun updateHeadline(headline: HeadlineEntity)

    @Delete
    suspend fun deleteHeadline(headline: HeadlineEntity)

    @Query("DELETE FROM headlines WHERE country = :country")
    suspend fun clearCountry(country: String)

    @Query("DELETE FROM headlines")
    suspend fun clearAll()
    

    

    @OptIn(ExperimentalTime::class)
    @Query("""
        SELECT * FROM headlines 
        WHERE country = :country 
        AND ((:currentTime - cachedAt) < :ttlMs)
        ORDER BY publishedAt DESC
    """)
    suspend fun getValidCachedHeadlines(
        country: String,
        currentTime: Long = Clock.System.now().toEpochMilliseconds(),
        ttlMs: Long = 5 * 60 * 1000L
    ): List<HeadlineEntity>


    @Query("""
        SELECT * FROM headlines 
        WHERE country = :country 
        AND page = :page
        ORDER BY publishedAt DESC
    """)
    suspend fun getHeadlinesByPage(country: String, page: Int): List<HeadlineEntity>


    @OptIn(ExperimentalTime::class)
    @Query("""
        SELECT * FROM headlines 
        WHERE syncStatus IN ('PENDING', 'FAILED')
        OR isStale = 1
        OR ((:currentTime - cachedAt) > :maxStalenessMs)
        ORDER BY lastModified ASC
    """)
    suspend fun getHeadlinesNeedingSync(
        currentTime: Long = Clock.System.now().toEpochMilliseconds(),
        maxStalenessMs: Long = 30 * 60 * 1000L
    ): List<HeadlineEntity>


    @OptIn(ExperimentalTime::class)
    @Query("""
        SELECT COUNT(*) > 0 FROM headlines 
        WHERE country = :country 
        AND ((:currentTime - cachedAt) < :ttlMs)
    """)
    suspend fun hasValidCache(
        country: String,
        currentTime: Long = Clock.System.now().toEpochMilliseconds(),
        ttlMs: Long = 5 * 60 * 1000L
    ): Boolean


    @Query("""
        SELECT MAX(cachedAt) FROM headlines 
        WHERE country = :country
    """)
    suspend fun getNewestCacheTime(country: String): Long?


    @Query("""
        SELECT MIN(cachedAt) FROM headlines 
        WHERE country = :country
    """)
    suspend fun getOldestCacheTime(country: String): Long?


    @Query("SELECT COUNT(*) FROM headlines WHERE syncStatus = :status")
    suspend fun countByStatus(status: SyncStatus): Int

    /**
     * Count total cached headlines for a country
     */
    @Query("SELECT COUNT(*) FROM headlines WHERE country = :country")
    suspend fun countCachedHeadlines(country: String): Int


    
    /**
     * Delete expired headlines older than TTL
     */
    @OptIn(ExperimentalTime::class)
    @Query("""
        DELETE FROM headlines 
        WHERE ((:currentTime - cachedAt) > :ttlMs)
    """)
    suspend fun deleteExpiredHeadlines(
        currentTime: Long = Clock.System.now().toEpochMilliseconds(),
        ttlMs: Long = 24 * 60 * 60 * 1000L // 24 hours default cleanup TTL
    ): Int

    /**
     * Delete headlines beyond the cache size limit (keep most recent)
     */
    @Query("""
        DELETE FROM headlines 
        WHERE id NOT IN (
            SELECT id FROM headlines 
            WHERE country = :country
            ORDER BY cachedAt DESC 
            LIMIT :maxEntries
        )
        AND country = :country
    """)
    suspend fun limitCacheSize(country: String, maxEntries: Int = 500): Int

    /**
     * Mark all headlines as stale for a country (forces refresh)
     */
    @OptIn(ExperimentalTime::class)
    @Query("""
        UPDATE headlines 
        SET isStale = 1, syncStatus = 'PENDING', lastModified = :currentTime
        WHERE country = :country
    """)
    suspend fun markCountryAsStale(
        country: String, 
        currentTime: Long = Clock.System.now().toEpochMilliseconds()
    )

    /**
     * Update sync status for specific headlines
     */
    @OptIn(ExperimentalTime::class)
    @Query("""
        UPDATE headlines 
        SET syncStatus = :status, lastModified = :currentTime
        WHERE id IN (:ids)
    """)
    suspend fun updateSyncStatus(
        ids: List<String>, 
        status: SyncStatus,
        currentTime: Long = Clock.System.now().toEpochMilliseconds()
    )

    /**
     * Get cache statistics for monitoring
     */
    @OptIn(ExperimentalTime::class)
    @Query("""
        SELECT 
            COUNT(*) as total,
            COUNT(CASE WHEN ((:currentTime - cachedAt) < :ttlMs) THEN 1 END) as valid,
            COUNT(CASE WHEN syncStatus = 'SYNCED' THEN 1 END) as synced,
            COUNT(CASE WHEN isStale = 1 THEN 1 END) as stale,
            MIN(cachedAt) as oldest,
            MAX(cachedAt) as newest,
            AVG(:currentTime - cachedAt) as avgAge
        FROM headlines 
        WHERE country = :country
    """)
    suspend fun getCacheStats(
        country: String,
        currentTime: Long = Clock.System.now().toEpochMilliseconds(),
        ttlMs: Long = 5 * 60 * 1000L
    ): CacheStatsEntity

    /**
     * Get headlines for accessibility - includes cache age information
     */
    @OptIn(ExperimentalTime::class)
    @Query("""
        SELECT *, (:currentTime - cachedAt) as ageMs
        FROM headlines 
        WHERE country = :country
        ORDER BY publishedAt DESC
        LIMIT :limit
    """)
    suspend fun getHeadlinesWithAge(
        country: String,
        limit: Int = 20,
        currentTime: Long = Clock.System.now().toEpochMilliseconds()
    ): List<HeadlineWithAge>
}

/**
 * Cache statistics data class for monitoring
 */
data class CacheStatsEntity(
    val total: Int,
    val valid: Int,
    val synced: Int,
    val stale: Int,
    val oldest: Long,
    val newest: Long,
    val avgAge: Double
)

/**
 * Headline with age information for accessibility
 */
data class HeadlineWithAge(
    val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val source: String,
    val cachedAt: Long,
    val country: String,
    val page: Int,
    val syncStatus: SyncStatus,
    val isStale: Boolean,
    val lastModified: Long,
    val ageMs: Long
) {
    /**
     * Convert to HeadlineEntity
     */
    fun toEntity(): HeadlineEntity = HeadlineEntity(
        id = id,
        title = title,
        description = description,
        url = url,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        source = source,
        cachedAt = cachedAt,
        country = country,
        page = page,
        syncStatus = syncStatus,
        isStale = isStale,
        lastModified = lastModified
    )
}
