package com.kashif.kmmnewsapp.feature.headlines.domain

data class Headline(
    val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val source: String
)
