package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

sealed interface HeadlinesEffect {
    data class ShowError(val message: String) : HeadlinesEffect
}
