package com.kashif.kmmnewsapp.data.remote.service

import com.kashif.kmmnewsapp.data.remote.dto.HeadlinesDTO

abstract class AbstractKtorService {
    abstract suspend fun getHeadLines(
        pageSize: Int,
        page: Int,
        country: String,
    ): HeadlinesDTO

}

