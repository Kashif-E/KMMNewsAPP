package com.kashif.kmmnewsapp.feature.headlines.domain

class GetTopHeadlinesUseCase(private val repository: HeadlineRepository) {
    suspend operator fun invoke() = repository.getCachedHeadlines()
}
