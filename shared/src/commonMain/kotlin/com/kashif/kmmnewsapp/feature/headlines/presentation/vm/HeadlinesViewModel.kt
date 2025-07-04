package com.kashif.kmmnewsapp.feature.headlines.presentation.vm

import co.touchlab.skie.configuration.annotations.FlowInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.kashif.kmmnewsapp.core.pagination.PaginationIntent
import com.kashif.kmmnewsapp.core.pagination.PaginationManager
import com.kashif.kmmnewsapp.core.pagination.PaginationResult
import com.kashif.kmmnewsapp.feature.headlines.domain.GetCachedHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.Headline
import com.kashif.kmmnewsapp.feature.headlines.domain.LoadHeadlinesPageUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.RefreshHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesEffect
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesIntent
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Production-ready ViewModel with advanced pagination capabilities.
 *
 * Key improvements based on research findings:
 * - Uses dedicated PaginationManager for robust state management
 * - Implements proper error handling and recovery mechanisms
 * - Supports accessibility with descriptive states
 * - Provides thread-safe operations
 * - Includes debouncing to prevent duplicate requests
 * - Maintains backward compatibility with existing MVI pattern
 *
 * @param defaultCountry The default country code for loading headlines
 * @param defaultPageSize The default page size for pagination
 */
class HeadlinesViewModel(
    private val defaultCountry: String = "us",
    private val defaultPageSize: Int = 20
) : ViewModel(), KoinComponent {

    // Use cases injected via Koin
    private val loadHeadlinesPage: LoadHeadlinesPageUseCase = get()
    private val refreshHeadlines: RefreshHeadlinesUseCase = get()
    private val getCachedHeadlines: GetCachedHeadlinesUseCase = get()

    // Advanced pagination manager with production-ready features
    private val paginationManager = PaginationManager<Headline>()

    // Legacy state for backward compatibility
    private val _state = MutableStateFlow(viewModelScope, HeadlinesState())

    /**
     * Public state that maps from the new pagination state to legacy state format
     * This maintains backward compatibility while providing enhanced features
     */
    @FlowInterop.Enabled
    val state = paginationManager.state.map { paginationState ->
        HeadlinesState(
            headlines = paginationState.items,
            currentPage = paginationState.currentPage,
            isLoading = paginationState.isInitialLoading,
            isLoadingNextPage = paginationState.isLoadingMore,
            hasMore = paginationState.hasMore,
            error = paginationState.error?.message
        )
    }

    /**
     * Enhanced pagination state for advanced features
     */
    @FlowInterop.Enabled
    val paginationState = paginationManager.state

    // Effects for one-time events
    private val _effect = MutableSharedFlow<HeadlinesEffect>()
    @FlowInterop.Enabled
    val effect: SharedFlow<HeadlinesEffect> = _effect.asSharedFlow()

    // Current country being displayed
    private var currentCountry: String = defaultCountry

    init {
        // Load initial data when ViewModel is created
        loadInitialData()
    }

    /**
     * Enhanced intent handler with improved pagination support
     */
    @FunctionInterop.LegacyName.Disabled
    fun send(intent: HeadlinesIntent, country: String? = null) {
        println("📨 Enhanced pagination - Received intent: $intent, country: $country")
        
        // Update current country if provided
        country?.let { currentCountry = it }

        viewModelScope.launch {
            when (intent) {
                is HeadlinesIntent.LoadInitial -> {
                    handlePaginationIntent(PaginationIntent.LoadInitial)
                }

                is HeadlinesIntent.RefreshHeadlines -> {
                    handlePaginationIntent(PaginationIntent.Refresh)
                }

                is HeadlinesIntent.LoadNextPage -> {
                    handlePaginationIntent(PaginationIntent.LoadMore)
                }
            }
        }
    }

    /**
     * New pagination intent handler for enhanced features
     */
    @FunctionInterop.LegacyName.Disabled
    fun sendPaginationIntent(intent: PaginationIntent, country: String? = null) {
        println("📨 sendPaginationIntent called with: $intent")
        country?.let { currentCountry = it }
        
        viewModelScope.launch {
            handlePaginationIntent(intent)
        }
    }

    /**
     * Centralized pagination intent handling
     */
    private suspend fun handlePaginationIntent(intent: PaginationIntent) {
        try {
            println("🎯 handlePaginationIntent: $intent")
            when (intent) {
                is PaginationIntent.LoadInitial -> {
                    println("🚀 Loading initial page with size: $defaultPageSize")
                    paginationManager.loadInitial(defaultPageSize) { page, size ->
                        loadHeadlinesData(page, size)
                    }
                }

                is PaginationIntent.LoadMore -> {
                    println("📄 Loading next page")
                    paginationManager.loadNext { page, size ->
                        loadHeadlinesData(page, size)
                    }
                }

                is PaginationIntent.Refresh -> {
                    println("🔄 Refreshing data")
                    // Clear cache before refresh
                    refreshHeadlines(currentCountry)
                    paginationManager.refresh { page, size ->
                        loadHeadlinesData(page, size)
                    }
                }

                is PaginationIntent.Retry -> {
                    println("🔁 Retrying last operation")
                    paginationManager.retry { page, size ->
                        loadHeadlinesData(page, size)
                    }
                }

                is PaginationIntent.Clear -> {
                    println("🧹 Clearing pagination state")
                    paginationManager.clear()
                }

                is PaginationIntent.SetPageSize -> {
                    println("📏 Setting page size to: ${intent.size}")
                    // Reset with new page size
                    paginationManager.clear()
                    paginationManager.loadInitial(intent.size) { page, size ->
                        loadHeadlinesData(page, size)
                    }
                }
            }
        } catch (e: Exception) {
            println("❌ Error handling pagination intent: ${e.message}")
            _effect.emit(HeadlinesEffect.ShowError(e.message ?: "Unknown error"))
        }
    }

    /**
     * Load headlines data with improved error handling
     */
    private suspend fun loadHeadlinesData(page: Int, pageSize: Int): PaginationResult<Headline> {
        println("🔄 Loading page $page with pageSize=$pageSize")
        
        val (headlines, totalResults) = loadHeadlinesPage(
            country = currentCountry,
            page = page,
            pageSize = pageSize,
            append = page > 1
        )

        println("✅ Loaded ${headlines.size} headlines, totalResults=$totalResults")

        // Calculate if there are more pages available
        val totalPages = (totalResults + pageSize - 1) / pageSize
        val hasMore = page < totalPages && headlines.isNotEmpty()

        return PaginationResult(
            items = headlines,
            totalItems = totalResults,
            hasMore = hasMore
        )
    }

    /**
     * Load initial data with cache fallback
     * FIXED: Don't collect cache flow forever, just load fresh data immediately
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                println("🚀 Starting initial data load")
                // Load fresh data immediately - cache can be handled separately if needed
                handlePaginationIntent(PaginationIntent.LoadInitial)
            } catch (e: Exception) {
                println("❌ Error loading initial data: ${e.message}")
                _effect.emit(HeadlinesEffect.ShowError(e.message ?: "Failed to load headlines"))
            }
        }
    }

    /**
     * Determines if more content should be loaded based on scroll position
     * This is called from the UI when user scrolls near the end
     */
    fun shouldLoadMore(lastVisibleIndex: Int): Boolean {
        val currentState = paginationManager.state.value
        return currentState.shouldLoadMore(lastVisibleIndex, bufferSize = 3)
    }

    /**
     * Provides accessibility description for screen readers
     */
    fun getAccessibilityDescription(): String {
        return paginationManager.state.value.accessibilityDescription
    }

    /**
     * Get loading progress for progress indicators
     */
    fun getLoadingProgress(): Float {
        return paginationManager.state.value.loadingProgress
    }

    /**
     * Check if retry is available after an error
     */
    fun canRetry(): Boolean {
        val error = paginationManager.state.value.error
        return error != null && error.isRecoverable
    }

    /**
     * Trigger retry after an error
     */
    fun retryLastOperation() {
        viewModelScope.launch {
            handlePaginationIntent(PaginationIntent.Retry)
        }
    }
}
