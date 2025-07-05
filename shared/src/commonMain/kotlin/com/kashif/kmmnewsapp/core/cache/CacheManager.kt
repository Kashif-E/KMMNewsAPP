package com.kashif.kmmnewsapp.core.cache

import com.kashif.kmmnewsapp.core.cache.CacheManager.Companion.DEFAULT_TTL_MS
import com.kashif.kmmnewsapp.core.cache.CacheManager.Companion.EVICTION_BUFFER_SIZE
import com.kashif.kmmnewsapp.core.cache.CacheManager.Companion.MAX_CACHE_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock


interface CacheManager {

    val cacheState: StateFlow<CacheState>


    suspend fun isCacheValid(key: String, maxAgeMs: Long = DEFAULT_TTL_MS): Boolean


    suspend fun getCacheAge(key: String): Long?


    suspend fun invalidateCache(key: String)


    suspend fun clearAll()


    suspend fun cleanup(maxAgeMs: Long = DEFAULT_TTL_MS): Int


    suspend fun getCacheStats(): CacheStats

    companion object {

        const val DEFAULT_TTL_MS = 5 * 60 * 1000L


        const val MAX_CACHE_SIZE = 1000


        const val EVICTION_BUFFER_SIZE = 10


        const val MIN_HEALTH_PERCENTAGE = 70
    }
}


data class CacheState(
    val totalEntries: Int = 0,
    val validEntries: Int = 0,
    val lastCleanupTime: Long = 0L,
    val memoryUsageBytes: Long = 0L,
    val accessibilityDescription: String = "Cache is empty"
) {
    init {
        require(totalEntries >= 0) { "Total entries cannot be negative: $totalEntries" }
        require(validEntries >= 0) { "Valid entries cannot be negative: $validEntries" }
        require(validEntries <= totalEntries) { "Valid entries ($validEntries) cannot exceed total entries ($totalEntries)" }
        require(memoryUsageBytes >= 0) { "Memory usage cannot be negative: $memoryUsageBytes" }
        require(accessibilityDescription.isNotBlank()) { "Accessibility description cannot be blank" }
    }


    val healthPercentage: Int
        get() = if (totalEntries == 0) 100 else (validEntries * 100) / totalEntries


    val needsCleanup: Boolean
        get() = totalEntries > CacheManager.MAX_CACHE_SIZE || healthPercentage < CacheManager.MIN_HEALTH_PERCENTAGE
}


data class CacheStats(
    val hitCount: Long = 0L,
    val missCount: Long = 0L,
    val evictionCount: Long = 0L,
    val averageAge: Long = 0L,
    val oldestEntry: Long = 0L,
    val newestEntry: Long = 0L
) {
    init {
        require(hitCount >= 0) { "Hit count cannot be negative: $hitCount" }
        require(missCount >= 0) { "Miss count cannot be negative: $missCount" }
        require(evictionCount >= 0) { "Eviction count cannot be negative: $evictionCount" }
        require(averageAge >= 0) { "Average age cannot be negative: $averageAge" }
    }


    val hitRate: Double
        get() = if (hitCount + missCount == 0L) 0.0 else hitCount.toDouble() / (hitCount + missCount)


    val totalOperations: Long
        get() = hitCount + missCount
}


data class CacheEntry(
    val key: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val lastAccessedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val accessCount: Long = 1L,
    val sizeBytes: Long = 0L
) {
    init {
        require(key.isNotBlank()) { "Cache key cannot be blank" }
        require(createdAt > 0) { "Created timestamp must be positive: $createdAt" }
        require(lastAccessedAt > 0) { "Last accessed timestamp must be positive: $lastAccessedAt" }
        require(lastAccessedAt >= createdAt) { "Last accessed ($lastAccessedAt) cannot be before created ($createdAt)" }
        require(accessCount > 0) { "Access count must be positive: $accessCount" }
        require(sizeBytes >= 0) { "Size cannot be negative: $sizeBytes" }
    }


    val ageMs: Long
        get() = maxOf(0L, Clock.System.now().toEpochMilliseconds() - createdAt)


    fun isExpired(ttlMs: Long): Boolean {
        require(ttlMs > 0) { "TTL must be positive: $ttlMs" }
        return ageMs > ttlMs
    }


    fun withAccess(): CacheEntry = copy(
        lastAccessedAt = Clock.System.now().toEpochMilliseconds(),
        accessCount = accessCount + 1
    )
}


class CacheManagerImpl : CacheManager {


    private val mutex = Mutex()


    private val cacheEntries = mutableMapOf<String, CacheEntry>()


    private var stats = CacheStats()


    private val _cacheState = MutableStateFlow(CacheState())


    override val cacheState: StateFlow<CacheState> = _cacheState.asStateFlow()

    override suspend fun isCacheValid(key: String, maxAgeMs: Long): Boolean {
        require(key.isNotBlank()) { "Cache key cannot be blank" }
        require(maxAgeMs > 0) { "Max age must be positive: $maxAgeMs" }

        return mutex.withLock {
            val entry = cacheEntries[key]

            when {
                entry == null -> {

                    stats = stats.copy(missCount = stats.missCount + 1)
                    updateCacheStateInternal()
                    false
                }

                entry.isExpired(maxAgeMs) -> {

                    cacheEntries.remove(key)
                    stats = stats.copy(missCount = stats.missCount + 1)
                    updateCacheStateInternal()
                    false
                }

                else -> {

                    cacheEntries[key] = entry.withAccess()
                    stats = stats.copy(hitCount = stats.hitCount + 1)
                    updateCacheStateInternal()
                    true
                }
            }
        }
    }

    override suspend fun getCacheAge(key: String): Long? {
        require(key.isNotBlank()) { "Cache key cannot be blank" }

        return mutex.withLock {
            cacheEntries[key]?.ageMs
        }
    }

    override suspend fun invalidateCache(key: String) {
        require(key.isNotBlank()) { "Cache key cannot be blank" }

        mutex.withLock {
            val removed = cacheEntries.remove(key)
            if (removed != null) {
                updateCacheStateInternal()
            }
        }
    }

    override suspend fun clearAll() = mutex.withLock {
        cacheEntries.clear()
        stats = CacheStats()
        updateCacheStateInternal()
    }

    override suspend fun cleanup(maxAgeMs: Long): Int {
        require(maxAgeMs > 0) { "Max age must be positive: $maxAgeMs" }

        return mutex.withLock {
            val expiredKeys = cacheEntries.keys.filter { key ->
                cacheEntries[key]?.isExpired(maxAgeMs) == true
            }

            expiredKeys.forEach { key ->
                cacheEntries.remove(key)
            }

            if (expiredKeys.isNotEmpty()) {
                stats = stats.copy(evictionCount = stats.evictionCount + expiredKeys.size)
                updateCacheStateInternal()
            }

            expiredKeys.size
        }
    }

    override suspend fun getCacheStats(): CacheStats = mutex.withLock {
        val entries = cacheEntries.values
        val now = Clock.System.now().toEpochMilliseconds()

        when {
            entries.isEmpty() -> stats.copy(
                averageAge = 0L,
                oldestEntry = 0L,
                newestEntry = 0L
            )

            else -> {
                val ages = entries.map { now - it.createdAt }
                stats.copy(
                    averageAge = ages.average().toLong(),
                    oldestEntry = entries.minOf { it.createdAt },
                    newestEntry = entries.maxOf { it.createdAt }
                )
            }
        }
    }


    suspend fun registerEntry(key: String, sizeBytes: Long = 0L) {
        require(key.isNotBlank()) { "Cache key cannot be blank" }
        require(sizeBytes >= 0) { "Size cannot be negative: $sizeBytes" }

        mutex.withLock {
            cacheEntries[key] = CacheEntry(key = key, sizeBytes = sizeBytes)


            if (cacheEntries.size > MAX_CACHE_SIZE) {
                evictLeastRecentlyUsedInternal()
            }

            updateCacheStateInternal()
        }
    }


    private fun evictLeastRecentlyUsedInternal() {
        val entriesToRemove = cacheEntries.size - MAX_CACHE_SIZE + EVICTION_BUFFER_SIZE

        if (entriesToRemove <= 0) return


        val keysToRemove = cacheEntries.values
            .sortedBy { it.lastAccessedAt }
            .take(entriesToRemove)
            .map { it.key }

        keysToRemove.forEach { key ->
            cacheEntries.remove(key)
        }

        stats = stats.copy(evictionCount = stats.evictionCount + keysToRemove.size)
    }


    private fun updateCacheStateInternal() {
        val totalEntries = cacheEntries.size
        val now = Clock.System.now().toEpochMilliseconds()
        val validEntries = cacheEntries.values.count { !it.isExpired(DEFAULT_TTL_MS) }
        val memoryUsage = cacheEntries.values.sumOf { it.sizeBytes }

        val accessibilityDescription = when {
            totalEntries == 0 -> "Cache is empty"
            validEntries == totalEntries -> "$totalEntries items cached, all current"
            validEntries == 0 -> "$totalEntries items cached, all expired"
            else -> "$validEntries current items, ${totalEntries - validEntries} expired"
        }

        _cacheState.update { currentState ->
            currentState.copy(
                totalEntries = totalEntries,
                validEntries = validEntries,
                lastCleanupTime = now,
                memoryUsageBytes = memoryUsage,
                accessibilityDescription = accessibilityDescription
            )
        }
    }
}