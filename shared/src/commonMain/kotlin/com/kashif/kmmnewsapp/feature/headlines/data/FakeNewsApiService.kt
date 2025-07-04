package com.kashif.kmmnewsapp.feature.headlines.data

import com.kashif.kmmnewsapp.core.network.HeadlineDto
import com.kashif.kmmnewsapp.core.network.NewsApiService
import com.kashif.kmmnewsapp.core.network.SourceDto

class FakeNewsApiService : NewsApiService {
    override suspend fun getTopHeadlines(): List<HeadlineDto> = listOf(
        HeadlineDto(
            title = "Test Headline 1",
            description = "Description 1",
            url = "https://example.com/1",
            imageUrl = null,
            publishedAt = "2023-01-01T00:00:00Z",
            source = SourceDto(id = "test", name = "Example Source")
        ),
        HeadlineDto(
            title = "Test Headline 2",
            description = "Description 2",
            url = "https://example.com/2",
            imageUrl = null,
            publishedAt = "2023-01-02T00:00:00Z",
            source = SourceDto(id = "test", name = "Example Source")
        )
    )
}
