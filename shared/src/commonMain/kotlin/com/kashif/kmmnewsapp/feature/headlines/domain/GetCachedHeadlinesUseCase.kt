package com.kashif.kmmnewsapp.feature.headlines.domain

class GetCachedHeadlinesUseCase(private val repository: HeadlineRepository) {
    suspend operator fun invoke() = repository.getCachedHeadlines()
}
