package com.kashif.kmmnewsapp.feature.headlines.domain

class LoadHeadlinesPageUseCase(private val repository: HeadlineRepository) {
    suspend operator fun invoke(country: String, page: Int, pageSize: Int, append: Boolean): Pair<List<Headline>, Int> =
        repository.loadHeadlinesPage(country, page, pageSize, append)
}
