package com.kashif.kmmnewsapp.core.network

import com.kashif.kmmnewsapp.core.database.HeadlineEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String = ""
)

interface NewsApiService {
    suspend fun getTopHeadlines(): List<HeadlineDto>
}

@Serializable
data class HeadlineDto(
    val title: String = "",
    val description: String? = null,
    val url: String = "",
    @SerialName("urlToImage") val imageUrl: String? = null,
    val publishedAt: String = "",
    val source: SourceDto = SourceDto()
) {
    val id: String get() = url // Use URL as unique ID
}

@Serializable
data class NewsApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<HeadlineDto>
)

class NewsApiServiceImpl(private val client: HttpClient, private val apiKey: String) : NewsApiService {
    override suspend fun getTopHeadlines(): List<HeadlineDto> {
        val response: HttpResponse = client.get("https://newsapi.org/v2/top-headlines") {
            parameter("country", "us")
            parameter("apiKey", apiKey)
        }
        val apiResponse: NewsApiResponse = response.body()
        return apiResponse.articles
    }
}
