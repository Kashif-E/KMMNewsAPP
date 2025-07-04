package com.kashif.kmmnewsapp.feature.headlines.data

import com.kashif.kmmnewsapp.core.database.DatabaseProvider
import com.kashif.kmmnewsapp.core.database.HeadlineEntity
import com.kashif.kmmnewsapp.core.network.NewsApiService
import com.kashif.kmmnewsapp.feature.headlines.data.mapper.toDomain
import com.kashif.kmmnewsapp.feature.headlines.data.mapper.toEntity
import com.kashif.kmmnewsapp.feature.headlines.domain.Headline
import com.kashif.kmmnewsapp.feature.headlines.domain.HeadlineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HeadlineRepositoryImpl(
    private val newsApiService: NewsApiService,
    private val databaseProvider: DatabaseProvider
) : HeadlineRepository {
    override suspend fun getCachedHeadlines(): Flow<List<Headline>> =
        databaseProvider.getDatabase().headlinesDao().getAllHeadlines().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun refreshHeadlines(country: String) {
        val db = databaseProvider.getDatabase()
        // Only clear the cache - let the ViewModel handle the reload to avoid double API calls
        db.headlinesDao().clearAll()
    }

    override suspend fun loadHeadlinesPage(country: String, page: Int, pageSize: Int, append: Boolean): Pair<List<Headline>, Int> {
        val response = newsApiService.getTopHeadlines(country, page, pageSize)
        val entities = response.articles.map { it.toEntity() }
        val db = databaseProvider.getDatabase()
        if (append) {
            val existing = db.headlinesDao().getAllHeadlinesOnce()
            val merged = (existing + entities).distinctBy { it.id }
            db.headlinesDao().clearAll()
            db.headlinesDao().insertHeadlines(merged)
        } else {
            db.headlinesDao().clearAll()
            db.headlinesDao().insertHeadlines(entities)
        }
        return Pair(entities.map { it.toDomain() }, response.totalResults)
    }
}
