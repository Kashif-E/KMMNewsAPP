package com.kashif.kmmnewsapp.data.repository

import com.kashif.kmmnewsapp.data.remote.dto.HeadlinesDTO


abstract class AbstractRepository {



    abstract suspend fun getAllHeadlines(
        page: Int,
        pageSize: Int,
        country: String
    ): HeadlinesDTO
}