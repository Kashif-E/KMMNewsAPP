package com.kashif.kmmnewsapp.feature.headlines.domain

import com.kashif.kmmnewsapp.feature.headlines.data.SyncStatusInfo
import kotlinx.coroutines.flow.Flow


interface HeadlineRepository {

    suspend fun getCachedHeadlines(country: String = "us"): Flow<List<Headline>>
    

    suspend fun refreshHeadlines(country: String)
    

    suspend fun loadHeadlinesPage(
        country: String, 
        page: Int, 
        pageSize: Int, 
        append: Boolean
    ): Pair<List<Headline>, Int>

    suspend fun getSyncStatus(country: String): SyncStatusInfo
}
