package com.kashif.kmmnewsapp.feature.headlines.domain

import kotlinx.coroutines.flow.Flow

interface HeadlineRepository {
   suspend fun getCachedHeadlines(): Flow<List<Headline>>
    suspend fun refreshHeadlines()
}
