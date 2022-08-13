package com.kashif.kmmnewsapp.data.remote.dto

import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel
import com.kashif.kmmnewsapp.domain.util.toLocalDate


@kotlinx.serialization.Serializable
data class HeadlinesDTO(

    val articles: List<Article>,


)

@kotlinx.serialization.Serializable
data class Source(

    val id: String? = "",

    val name: String
)

@kotlinx.serialization.Serializable
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

fun HeadlinesDTO.asDomainModel(): List<HeadlineDomainModel> {
    return this.articles.map {
           HeadlineDomainModel(
               author = it.author?: "Anonymous",
               content = it.content?:"",
               description = it.description?:"Click to see details",
               publishedAt = toLocalDate(it.publishedAt),
               source = it.source.name,
               title = it.title,
               urlToImage = it.urlToImage?:"",
               url = it.url
           )

    }
}