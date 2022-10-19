package com.kashif.kmmnewsapp.domain.usecase.readlater

import com.kashif.kmmnewsapp.data.local.dao.asDomainModel
import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import kotlinx.coroutines.flow.flow

class GetReadLaterUseCase(private val repository: AbstractRepository) {

    operator fun invoke() = flow {
        val response = repository.getAllReadLater().asDomainModel()

        emit(response)
    }
}