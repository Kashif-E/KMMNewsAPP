package com.kashif.kmmnewsapp.domain.domain_model

import com.kashif.kmmnewsapp.CommonParcelable
import com.kashif.kmmnewsapp.CommonParcelize



@CommonParcelize
data class ArticleDomainModel(

    val author: String,

    val content: String,

    val description: String,

    val publishedAt: String,

    val source: String,

    val title: String,

    val url: String,

    val urlToImage: String
) : CommonParcelable

