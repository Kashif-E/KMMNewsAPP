@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.kashif.kmmnewsapp.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock


@Entity(tableName = "headlines")
data class HeadlineEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val source: String,


    val cachedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val country: String = "us",
    val page: Int = 1,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val isStale: Boolean = false,
    val lastModified: Long = Clock.System.now().toEpochMilliseconds()
) {

    fun getCacheAge(): Long = Clock.System.now().toEpochMilliseconds() - cachedAt
    

    fun isExpired(ttlMs: Long = 5 * 60 * 1000L): Boolean = getCacheAge() > ttlMs
    

    fun needsSync(maxStalenessMs: Long = 30 * 60 * 1000L): Boolean {
        return syncStatus == SyncStatus.PENDING || 
               isStale || 
               getCacheAge() > maxStalenessMs
    }
    

    fun withSyncStatus(status: SyncStatus): HeadlineEntity = copy(
        syncStatus = status,
        lastModified = Clock.System.now().toEpochMilliseconds()
    )
    

    fun markAsStale(): HeadlineEntity = copy(
        isStale = true,
        syncStatus = SyncStatus.PENDING,
        lastModified = Clock.System.now().toEpochMilliseconds()
    )
}


enum class SyncStatus {

    SYNCED,
    

    PENDING,
    

    FAILED,
    

    LOCAL_ONLY
}
