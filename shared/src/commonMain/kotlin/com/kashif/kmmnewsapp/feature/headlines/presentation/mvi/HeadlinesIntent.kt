package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

sealed interface HeadlinesIntent {
    object LoadInitial : HeadlinesIntent
    object RefreshHeadlines : HeadlinesIntent
    object LoadNextPage : HeadlinesIntent
}
