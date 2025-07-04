package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

import com.kashif.kmmnewsapp.feature.headlines.domain.Headline

object HeadlinesReducer {
    fun reduce(state: HeadlinesState, intent: HeadlinesIntent, headlines: List<Headline>? = null, error: String? = null): HeadlinesState =
        when (intent) {
            is HeadlinesIntent.LoadHeadlines -> HeadlinesState.Loading
            is HeadlinesIntent.RefreshHeadlines -> HeadlinesState.Loading
        }.let {
            when {
                headlines != null -> HeadlinesState.Success(headlines)
                error != null -> HeadlinesState.Error(error)
                else -> it
            }
        }
}
