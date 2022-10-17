package com.kashif.kmmnewsapp.data.remote.service

import com.kashif.kmmnewsapp.data.remote.dto.HeadlinesDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ImplKtorService(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : AbstractKtorService() {

    private val apikey = "a52b414d7a4e496a81b9787ebf8993f2"
    override suspend fun getHeadLines(
        pageSize: Int,
        page: Int,
        country: String,

    ): HeadlinesDTO = httpClient.get("$baseUrl/${EndPoints.HEADLINES}") {
        header("x-api-key", apikey)
        parameter("country", country)
        parameter("pageSize", pageSize)
        parameter("page", page)
    }.body()

    }

