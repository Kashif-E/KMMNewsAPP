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
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class HeadlinesViewModel(
    private val defaultCountry: String = "us",
    private val defaultPageSize: Int = 20
) : ViewModel(), KoinComponent {


    private val loadHeadlinesPage: LoadHeadlinesPageUseCase = get()
    private val refreshHeadlines: RefreshHeadlinesUseCase = get()
    private val getCachedHeadlines: GetCachedHeadlinesUseCase = get()


    private val paginationManager = PaginationManager<Headline>()



    @FlowInterop.Enabled
    val paginationState = paginationManager.state


    private val _effect = MutableSharedFlow<HeadlinesEffect>()

    @FlowInterop.Enabled
    val effect: SharedFlow<HeadlinesEffect> = _effect.asSharedFlow()


    private val _lastSyncTime = MutableStateFlow<Long?>(null)
    val lastSyncTime: StateFlow<Long?> = _lastSyncTime


    private var currentCountry: String = defaultCountry

    init {

        loadInitialData()
    }


    @FunctionInterop.LegacyName.Disabled
    fun send(intent: HeadlinesIntent, country: String? = null) {


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


    @FunctionInterop.LegacyName.Disabled
    fun sendPaginationIntent(intent: PaginationIntent, country: String? = null) {

        country?.let { currentCountry = it }

        viewModelScope.launch {
            handlePaginationIntent(intent)
        }
    }


    private suspend fun handlePaginationIntent(intent: PaginationIntent) {
        try {
            when (intent) {
                is PaginationIntent.LoadInitial -> {
                    paginationManager.loadInitial(defaultPageSize) { page, size ->
                        loadHeadlinesData(page, size)
                    }
                    updateLastSyncTime()
                }

                is PaginationIntent.LoadMore -> {
                    paginationManager.loadNext { page, size ->
                        loadHeadlinesData(page, size)
                    }
                    updateLastSyncTime()
                }

                is PaginationIntent.Refresh -> {

                    refreshHeadlines(currentCountry)
                    paginationManager.refresh { page, size ->
                        loadHeadlinesData(page, size)
                    }
                    updateLastSyncTime()
                }

                is PaginationIntent.Retry -> {
                    paginationManager.retry { page, size ->
                        loadHeadlinesData(page, size)
                    }
                    updateLastSyncTime()
                }

                is PaginationIntent.Clear -> {
                    paginationManager.clear()
                    _lastSyncTime.value = null
                }

                is PaginationIntent.SetPageSize -> {

                    paginationManager.clear()
                    paginationManager.loadInitial(intent.size) { page, size ->
                        loadHeadlinesData(page, size)
                    }
                    updateLastSyncTime()
                }
            }
        } catch (e: Exception) {
            _effect.emit(HeadlinesEffect.ShowError(e.message ?: "Unknown error"))
        }
    }


    private suspend fun loadHeadlinesData(page: Int, pageSize: Int): PaginationResult<Headline> {

        val (headlines, totalResults) = loadHeadlinesPage(
            country = currentCountry,
            page = page,
            pageSize = pageSize,
            append = page > 1
        )


        val totalPages = (totalResults + pageSize - 1) / pageSize
        val hasMore = page < totalPages && headlines.isNotEmpty()

        return PaginationResult(
            items = headlines,
            totalItems = totalResults,
            hasMore = hasMore
        )
    }


    private fun loadInitialData() {
        viewModelScope.launch {
            try {

                handlePaginationIntent(PaginationIntent.LoadInitial)
            } catch (e: Exception) {
                _effect.emit(HeadlinesEffect.ShowError(e.message ?: "Failed to load headlines"))
            }
        }
    }


    fun shouldLoadMore(lastVisibleIndex: Int): Boolean {
        val currentState = paginationManager.state.value
        return currentState.shouldLoadMore(lastVisibleIndex, bufferSize = 3)
    }


    fun getAccessibilityDescription(): String {
        return paginationManager.state.value.accessibilityDescription
    }


    fun getLoadingProgress(): Float {
        return paginationManager.state.value.loadingProgress
    }


    fun canRetry(): Boolean {
        val error = paginationManager.state.value.error
        return error != null && error.isRecoverable
    }


    fun retryLastOperation() {
        viewModelScope.launch {
            handlePaginationIntent(PaginationIntent.Retry)
        }
    }

    private suspend fun updateLastSyncTime() {
        val status = getCachedHeadlines.getSyncStatus(currentCountry)
        _lastSyncTime.update {
            status.lastSyncTime
        }
    }
}
