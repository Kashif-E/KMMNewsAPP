package com.kashif.kmmnewsapp.feature.headlines.domain

import com.kashif.kmmnewsapp.feature.headlines.data.SyncStatusInfo

class GetCachedHeadlinesUseCase(private val repository: HeadlineRepository) {
    suspend operator fun invoke() = repository.getCachedHeadlines()

    suspend fun getSyncStatus(country: String): SyncStatusInfo {
        return repository.getSyncStatus(country)
    }
}
