package com.kashif.kmmnewsapp.domain.usecase.home

import com.kashif.kmmnewsapp.data.remote.dto.asDomainModel
import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import kotlinx.coroutines.flow.flow

class GetHeadlinesUseCase(
    private val repository: AbstractRepository
) {

    operator fun invoke(page: Int, pageSize: Int = 20, country: String = "us") = flow {

        val response =
            repository.getAllHeadlines(page = page, pageSize = pageSize, country = country)
                .asDomainModel()

        emit(response)

    }
}