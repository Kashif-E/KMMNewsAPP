package com.kashif.kmmnewsapp.presentation.home

import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel

data class HomeScreenState(
    val isLoading: Boolean = true,
    val headlines: List<HeadlineDomainModel> = emptyList(),
    val error: Error = Error(),
    val isSuccess: Boolean = false,
    val page: Int = 1
)

data class Error(
    val isError: Boolean = false,
    val errorMessage: String = "Something went wrong."
)