package com.kashif.kmmnewsapp.feature.headlines.presentation.mvi

import com.kashif.kmmnewsapp.feature.headlines.domain.Headline

// Use a data class for state for easier copy and mutation

data class HeadlinesState(
    val headlines: List<Headline> = emptyList(),
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val hasMore: Boolean = true,
    val error: String? = null
)
