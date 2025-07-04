package com.kashif.kmmnewsapp.feature.headlines.domain

import kotlinx.coroutines.flow.Flow

interface HeadlineRepository {
    suspend fun getCachedHeadlines(): Flow<List<Headline>>
    suspend fun refreshHeadlines(country: String)
    suspend fun loadHeadlinesPage(country: String, page: Int, pageSize: Int, append: Boolean): Pair<List<Headline>, Int>
}
