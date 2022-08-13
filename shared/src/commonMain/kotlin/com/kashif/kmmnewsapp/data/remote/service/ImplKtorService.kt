package com.kashif.kmmnewsapp.data.remote.service

import com.kashif.kmmnewsapp.data.remote.dto.HeadlinesDTO
import com.kashif.kmmnewsapp.domain.util.DataState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
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

    ): DataState<HeadlinesDTO> {

        return try {
            DataState.Success(httpClient.get("$baseUrl/${EndPoints.HEADLINES}") {
                header("x-api-key", apikey)
                parameter("country", country)
                parameter("pageSize", pageSize)
                parameter("page", page)
            }.body())
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            DataState.Error(DataState.CustomMessages.ExceptionMessage(e.response.status.description))
        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            DataState.Error(DataState.CustomMessages.ExceptionMessage(e.response.status.description))
        } catch (e: ServerResponseException) {
            // 5xx - responses
            DataState.Error(DataState.CustomMessages.ExceptionMessage(e.response.status.description))
        } catch (e: Exception) {
            println("Error: ${e.message}")
            DataState.Error(
                DataState.CustomMessages.ExceptionMessage(
                    e.message ?: "Something went wrong"
                )
            )
        }



    }

}