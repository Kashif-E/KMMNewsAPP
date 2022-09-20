package com.kashif.kmmnewsapp.data.repository

import com.kashif.kmmnewsapp.data.local.service.AbstractRealmService
import com.kashif.kmmnewsapp.data.remote.dto.HeadlinesDTO
import com.kashif.kmmnewsapp.data.remote.service.AbstractKtorService
import com.kashif.kmmnewsapp.domain.util.DataState
import io.ktor.client.plugins.*


class ImplRepository(
    private val ktorService: AbstractKtorService,
    private val realmService: AbstractRealmService
) : AbstractRepository() {


    override suspend fun getAllHeadlines(
        page: Int,
        pageSize: Int,
        country: String
    ): DataState<HeadlinesDTO> = try {

        val response = ktorService.getHeadLines(
            pageSize = pageSize,
            page = page,
            country = country

        )
        DataState.Success(response)
    } catch (e: Exception) {
        e.getRealException()
    }

}

fun <T> Exception.getRealException(): DataState<T> = when (this) {
    is HttpRequestTimeoutException -> {

        DataState.Error(DataState.CustomMessages.NetworkError)
    }
    is RedirectResponseException -> {
        DataState.Error(DataState.CustomMessages.NetworkError)
    }
    is ClientRequestException -> {
        DataState.Error(DataState.CustomMessages.NetworkError)
    }
    is ServerResponseException -> {
        DataState.Error(DataState.CustomMessages.ResponseError)
    }
    else -> {
        DataState.Error(DataState.CustomMessages.RandomError)
    }
}
