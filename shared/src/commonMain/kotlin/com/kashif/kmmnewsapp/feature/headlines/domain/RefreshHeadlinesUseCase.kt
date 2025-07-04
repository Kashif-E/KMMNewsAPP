package com.kashif.kmmnewsapp.feature.headlines.domain

class RefreshHeadlinesUseCase(private val repository: HeadlineRepository) {
    suspend operator fun invoke() = repository.refreshHeadlines()
}
