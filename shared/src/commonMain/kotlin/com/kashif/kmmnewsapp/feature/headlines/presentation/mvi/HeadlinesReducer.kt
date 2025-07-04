package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

import com.kashif.kmmnewsapp.feature.headlines.domain.Headline

object HeadlinesReducer {
    fun reduce(
        state: HeadlinesState,
        intent: HeadlinesIntent,
        headlines: List<Headline>? = null,
        error: String? = null,
        isLoading: Boolean = false,
        isLoadingNextPage: Boolean = false,
        hasMore: Boolean = true,
        currentPage: Int = 1
    ): HeadlinesState {
        return when (intent) {
            is HeadlinesIntent.LoadInitial -> state.copy(isLoading = true, error = null)
            is HeadlinesIntent.RefreshHeadlines -> state.copy(isLoading = true, error = null, currentPage = 1, hasMore = true, headlines = emptyList())
            is HeadlinesIntent.LoadNextPage -> state.copy(isLoadingNextPage = true, error = null)
        }.let {
            when {
                headlines != null -> it.copy(
                    isLoading = false,
                    isLoadingNextPage = false,
                    error = null,
                    headlines = if (intent is HeadlinesIntent.LoadNextPage) state.headlines + headlines else headlines,
                    hasMore = hasMore,
                    currentPage = currentPage
                )
                error != null -> it.copy(
                    isLoading = false,
                    isLoadingNextPage = false,
                    error = error
                )
                else -> it
            }
        }
    }
}
