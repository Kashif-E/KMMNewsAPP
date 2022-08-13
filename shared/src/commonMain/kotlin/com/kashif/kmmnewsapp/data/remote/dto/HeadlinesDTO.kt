package com.kashif.kmmnewsapp.data.remote.dto

import com.kashif.kmmnewsapp.domain.domain_model.ArticleDomainModel



data class HeadlinesDTO(

    val articles: List<Article>,

    val status: String,

    val totalResults: Int
)


data class Source(

    val id: Any? = "",

    val name: String
)


data class Article(

    val author: String? = "Anonymous",

    val content: String? = "",

    val description: String? = "",

    val publishedAt: String,

    val source: Source,

    val title: String,

    val url: String,

    val urlToImage: String? = ""
)

fun List<Article>.asDomainModel(): List<ArticleDomainModel> {
    return map {
        ArticleDomainModel(
            author = it.author?: "Anonymous",
            content = it.content?:"",
            description = it.description?:"Click to see details",
            publishedAt = it.publishedAt,
            source = it.source.name,
            title = it.title,
            urlToImage = it.urlToImage?:"",
            url = it.url
        )
    }
}