package com.kashif.kmmnewsapp.domain.usecase

import com.kashif.kmmnewsapp.data.remote.dto.asDomainModel
import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import com.kashif.kmmnewsapp.domain.util.ResponseHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetHeadlinesUseCase(
    private val repository: AbstractRepository,
    private val responsehandler: ResponseHandler
) {

    operator fun invoke(page: Int, pageSize: Int = 20, country: String = "us") = flow {

        val response = repository.getAllHeadlines(page=page, pageSize=pageSize, country= country)


        emit(responsehandler.handleSuccess(response.data?.asDomainModel()))


    }.catch {
        emit(
            responsehandler.handleException(
                ((it.message ?: it.cause) ?: "Something Went wrong.").toString()
            )
        )
    }
}