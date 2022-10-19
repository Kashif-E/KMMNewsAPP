package com.kashif.kmmnewsapp.presentation.home

import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel


sealed interface HomeScreenState{

    object Loading: HomeScreenState

    object Idle : HomeScreenState

    data class Success(val headlines: List<HeadlineDomainModel>) : HomeScreenState

    data class Error(val errorMessage: String) : HomeScreenState



}

