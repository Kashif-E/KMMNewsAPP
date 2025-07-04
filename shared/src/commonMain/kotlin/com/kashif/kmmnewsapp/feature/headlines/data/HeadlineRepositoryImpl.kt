package com.kashif.kmmnewsapp.feature.headlines.data

import com.kashif.kmmnewsapp.core.database.DatabaseProvider
import com.kashif.kmmnewsapp.core.database.HeadlineEntity
import com.kashif.kmmnewsapp.core.database.HeadlinesDao
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

    override suspend fun refreshHeadlines() {
        val headlines = newsApiService.getTopHeadlines().map { it.toEntity() }
        val db = databaseProvider.getDatabase()
        db.headlinesDao().clearAll()
        db.headlinesDao().insertHeadlines(headlines)
    }
}
