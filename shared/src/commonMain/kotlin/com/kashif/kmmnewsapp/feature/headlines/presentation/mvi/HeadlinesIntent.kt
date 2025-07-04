package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

sealed interface HeadlinesIntent {
    object LoadHeadlines : HeadlinesIntent
    object RefreshHeadlines : HeadlinesIntent
}
