package com.kashif.kmmnewsapp.domain.usecase.newsdetails

import com.kashif.kmmnewsapp.data.repository.AbstractRepository
import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel
import com.kashif.kmmnewsapp.domain.domain_model.asDao

class AddToReadLaterUseCase(private val abstractRepository: AbstractRepository) {

    suspend operator fun invoke(headlineDomainModel: HeadlineDomainModel) {
        abstractRepository.addToReadLater(headlineDomainModel.asDao())
    }
}