package com.kashif.kmmnewsapp.presentation.readlater

import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel

public sealed interface ReadLaterState {
    object Loading : ReadLaterState
    data class Error(val message: String) : ReadLaterState
    data class Success(val headlines: List<HeadlineDomainModel>) : ReadLaterState
}