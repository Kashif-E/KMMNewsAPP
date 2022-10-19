package com.kashif.kmmnewsapp.domain.usecase

import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel
import com.kashif.kmmnewsapp.domain.domain_model.asDao

class AddToReadLaterUseCase(private val abstractRepository: AbstractRepository) {

    operator fun invoke(headlineDomainModel: HeadlineDomainModel) {
        abstractRepository.addToReadLater(headlineDomainModel.asDao())
    }
}