package com.kashif.kmmnewsapp.domain.usecase

import com.kashif.kmmnewsapp.data.remote.dto.asDomainModel
import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import com.kashif.kmmnewsapp.domain.util.DataState
import com.kashif.kmmnewsapp.domain.util.DataState.CustomMessages.ExceptionMessage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetHeadlinesUseCase(
    private val repository: AbstractRepository
) {

    operator fun invoke(page: Int, pageSize: Int, country: String = "us") = flow {

        val response = repository.getAllHeadlines(page, pageSize, country)

        if (response.data != null) {
            emit(response.also { it.data?.articles?.asDomainModel() })
        } else {
            emit(response)
        }


    }.catch {
        emit(
            DataState.Error(
                ExceptionMessage(
                    ((it.message ?: it.cause) ?: "Something Went wrong.").toString()
                )
            )
        )
    }
}