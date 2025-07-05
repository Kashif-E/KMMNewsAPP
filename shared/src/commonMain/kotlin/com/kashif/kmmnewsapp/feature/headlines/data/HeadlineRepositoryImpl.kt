package com.kashif.kmmnewsapp.feature.headlines.data

import com.kashif.kmmnewsapp.core.cache.CacheManager
import com.kashif.kmmnewsapp.core.cache.CacheManagerImpl
import com.kashif.kmmnewsapp.core.database.DatabaseProvider
import com.kashif.kmmnewsapp.core.database.SyncStatus
import com.kashif.kmmnewsapp.core.network.NetworkConnectivityService
import com.kashif.kmmnewsapp.core.network.NewsApiService
import com.kashif.kmmnewsapp.feature.headlines.data.mapper.toDomain
import com.kashif.kmmnewsapp.feature.headlines.data.mapper.toEntity
import com.kashif.kmmnewsapp.feature.headlines.domain.Headline
import com.kashif.kmmnewsapp.feature.headlines.domain.HeadlineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class HeadlineRepositoryImpl(
    private val newsApiService: NewsApiService,
    private val databaseProvider: DatabaseProvider,
    private val networkConnectivityService: NetworkConnectivityService,
    private val cacheManager: CacheManager = CacheManagerImpl()
) : HeadlineRepository {

    private val syncMutex = Mutex()
    private val backgroundSyncMutex = Mutex()
    
    companion object {
        private const val CACHE_TTL_MS = 5 * 60 * 1000L // 5 minutes
        private const val BACKGROUND_SYNC_INTERVAL_MS = 30 * 60 * 1000L
    }

    /**
     * Get cached headlines with offline-first strategy
     * Always returns cached data immediately, triggers background refresh if needed
     */
    override suspend fun getCachedHeadlines(country: String): Flow<List<Headline>> {
        // Start background sync if needed
        triggerBackgroundSyncIfNeeded(country)
        

        return combine(
            databaseProvider.getDatabase().headlinesDao().getAllHeadlines(country),
            cacheManager.cacheState,
            networkConnectivityService.connectivityState
        ) { headlines, cacheState, connectivityState ->
            headlines.map { entity ->
                val domainHeadline = entity.toDomain()
                domainHeadline
            }
        }
    }

    /**
     * Refresh headlines with intelligent sync strategy
     * Uses cache-first approach with background network updates
     */
    override suspend fun refreshHeadlines(country: String) {
        syncMutex.withLock {
        val db = databaseProvider.getDatabase()
        val dao = db.headlinesDao()
        
        try {
            if (networkConnectivityService.connectivityState.value.isConnected) {
                // Mark existing data as stale to trigger fresh fetch
                dao.markCountryAsStale(country)
                
                // Register cache invalidation
                cacheManager.invalidateCache("headlines_$country")
                
                // Trigger immediate refresh
                performNetworkSync(country, page = 1, isRefresh = true)
            } else {
                // Offline: Just mark cache as manually refreshed
            }
        } catch (e: Exception) {
            println(" Refresh failed: ${e.message}")

            val failedEntries = dao.getAllHeadlinesOnce(country)
            if (failedEntries.isNotEmpty()) {
                val ids = failedEntries.map { it.id }
                dao.updateSyncStatus(ids, SyncStatus.FAILED)
            }
            throw e
        }
        }
    }

    /**
     * Load headlines page with offline-first strategy and intelligent caching
     */
    override suspend fun loadHeadlinesPage(
        country: String, 
        page: Int, 
        pageSize: Int, 
        append: Boolean
    ): Pair<List<Headline>, Int> = syncMutex.withLock {
        
        val db = databaseProvider.getDatabase()
        val dao = db.headlinesDao()
        

        val hasValidCache = dao.hasValidCache(country, ttlMs = CACHE_TTL_MS)
        val isConnected = networkConnectivityService.connectivityState.value.isConnected
        
        return when {

            hasValidCache -> {
                println("Serving from valid cache for $country, page $page")
                val cachedHeadlines = dao.getValidCachedHeadlines(country, ttlMs = CACHE_TTL_MS)
                val totalCount = dao.countCachedHeadlines(country)
                

                val cacheManagerImpl = cacheManager as? CacheManagerImpl
                cacheManagerImpl?.registerEntry("headlines_${country}_$page", cachedHeadlines.size * 1024L)
                

                if (isConnected) {
                    triggerBackgroundSyncIfNeeded(country)
                }
                
                Pair(
                    cachedHeadlines.map { it.toDomain() },
                    totalCount
                )
            }
            

            isConnected -> {
                println("Fetching fresh data for $country, page $page")
                performNetworkSync(country, page, pageSize, append)
            }
            

            else -> {
                println("Offline mode - serving stale cache for $country")
                val staleHeadlines = dao.getAllHeadlinesOnce(country)
                val totalCount = staleHeadlines.size
                

                if (staleHeadlines.isNotEmpty()) {
                    val ids = staleHeadlines.map { it.id }
                    dao.updateSyncStatus(ids, SyncStatus.LOCAL_ONLY)
                }
                
                Pair(
                    staleHeadlines.map { it.toDomain() },
                    totalCount
                )
            }
        }
    }

    /**
     * Perform network synchronization with proper error handling and caching
     */
    @OptIn(ExperimentalTime::class)
    private suspend fun performNetworkSync(
        country: String,
        page: Int,
        pageSize: Int = 20,
        append: Boolean = false,
        isRefresh: Boolean = false
    ): Pair<List<Headline>, Int> {
        
        val db = databaseProvider.getDatabase()
        val dao = db.headlinesDao()
        
        try {

            val response = newsApiService.getTopHeadlines(country, page, pageSize)
            val currentTime = Clock.System.now().toEpochMilliseconds()
            

            val entities = response.articles.map { dto ->
                dto.toEntity().copy(
                    cachedAt = currentTime,
                    country = country,
                    page = page,
                    syncStatus = SyncStatus.SYNCED,
                    isStale = false,
                    lastModified = currentTime
                )
            }
            

            when {
                isRefresh || !append -> {

                    dao.clearCountry(country)
                    dao.insertHeadlines(entities)
                }
                append -> {

                    dao.insertHeadlines(entities)
                }
            }
            

            val cacheKey = "headlines_${country}_$page"
            val cacheManagerImpl = cacheManager as? CacheManagerImpl
            cacheManagerImpl?.registerEntry(cacheKey, entities.size * 1024L)
            

            if (dao.countCachedHeadlines(country) > 500) {
                dao.limitCacheSize(country, 400)
            }
            
            println("Network sync completed: ${entities.size} headlines for $country, page $page")
            
            return Pair(
                entities.map { it.toDomain() },
                response.totalResults
            )
            
        } catch (e: Exception) {
            println("Network sync failed for $country, page $page: ${e.message}")
            

            val existingEntries = dao.getHeadlinesByPage(country, page)
            if (existingEntries.isNotEmpty()) {
                val ids = existingEntries.map { it.id }
                dao.updateSyncStatus(ids, SyncStatus.FAILED)
            }
            
            throw e
        }
    }

    /**
     * Trigger background sync if cache is stale and network is available
     */
    private suspend fun triggerBackgroundSyncIfNeeded(country: String) {
        if (!backgroundSyncMutex.tryLock()) return // Already syncing
        
        try {
            val connectivityState = networkConnectivityService.connectivityState.value
            if (!connectivityState.shouldSync) return
            
            val db = databaseProvider.getDatabase()
            val dao = db.headlinesDao()
            
            // Check if background sync is needed
            val needsSync = !dao.hasValidCache(country, ttlMs = BACKGROUND_SYNC_INTERVAL_MS)
            
            if (needsSync) {
                println("Triggering background sync for $country")
                try {
                    performNetworkSync(country, page = 1, isRefresh = true)
                } catch (e: Exception) {
                    println("Background sync failed: ${e.message}")
                }
            }
        } finally {
            backgroundSyncMutex.unlock()
        }
    }

    /**
     * Get sync status for UI feedback
     */
    suspend fun getSyncStatus(country: String): SyncStatusInfo {
        val db = databaseProvider.getDatabase()
        val dao = db.headlinesDao()
        val cacheStats = dao.getCacheStats(country)
        val connectivityState = networkConnectivityService.connectivityState.value
        
        return SyncStatusInfo(
            isOnline = connectivityState.isConnected,
            totalCached = cacheStats.total,
            syncedCount = cacheStats.synced,
            failedCount = dao.countByStatus(SyncStatus.FAILED),
            pendingCount = dao.countByStatus(SyncStatus.PENDING),
            lastSyncTime = dao.getNewestCacheTime(country) ?: 0L,
            accessibilityDescription = generateSyncStatusDescription(cacheStats, connectivityState.isConnected)
        )
    }

    /**
     * Generate accessibility-friendly sync status description
     */
    private fun generateSyncStatusDescription(
        stats: com.kashif.kmmnewsapp.core.database.CacheStatsEntity,
        isOnline: Boolean
    ): String {
        return when {
            !isOnline && stats.total > 0 -> "Offline mode. Showing ${stats.total} cached articles."
            !isOnline && stats.total == 0 -> "Offline mode. No cached articles available."
            isOnline && stats.synced == stats.total -> "Online. All ${stats.total} articles are current."
            isOnline && stats.stale > 0 -> "Online. ${stats.synced} current, ${stats.stale} updating."
            else -> "Loading articles..."
        }
    }
}

/**
 * Sync status information for UI feedback
 */
data class SyncStatusInfo(
    val isOnline: Boolean,
    val totalCached: Int,
    val syncedCount: Int,
    val failedCount: Int,
    val pendingCount: Int,
    val lastSyncTime: Long,
    val accessibilityDescription: String
)


