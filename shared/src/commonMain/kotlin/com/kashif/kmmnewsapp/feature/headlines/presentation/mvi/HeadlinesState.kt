package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

import com.kashif.kmmnewsapp.feature.headlines.domain.Headline

sealed interface HeadlinesState {
    object Loading : HeadlinesState
    data class Success(val headlines: List<Headline>) : HeadlinesState
    data class Error(val message: String) : HeadlinesState
}
