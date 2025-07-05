package com.kashif.kmmnewsapp.core.pagination

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock


class PaginationManager<T> {

    private val _state = MutableStateFlow(PaginationState<T>())
    val state: StateFlow<PaginationState<T>> = _state.asStateFlow()


    private val mutex = Mutex()


    private var lastRequestTime = 0L
    private val debounceDelayMs = 300L


    suspend fun loadInitial(
        pageSize: Int = 20,
        loader: suspend (page: Int, size: Int) -> PaginationResult<T>
    ) {
        mutex.withLock {
            if (_state.value.isInitialLoading) {
                return@withLock
            }

            _state.update { current ->
                current.copy(
                    isInitialLoading = true,
                    error = null,
                    pageSize = pageSize
                )
            }

            try {
                val result = loader(1, pageSize)
                handleLoadResult(result, isInitial = true)
            } catch (e: Exception) {
                handleError(e, isInitial = true)
            }
        }
    }


    suspend fun loadNext(
        loader: suspend (page: Int, size: Int) -> PaginationResult<T>
    ) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - lastRequestTime < debounceDelayMs) {
            return
        }
        lastRequestTime = currentTime

        mutex.withLock {
            val current = _state.value
            if (!current.canLoadMore) {
                return@withLock
            }

            _state.update { it.copy(isLoadingMore = true, error = null) }

            try {
                val nextPage = current.currentPage + 1
                val result = loader(nextPage, current.pageSize)
                handleLoadResult(result, isInitial = false)
            } catch (e: Exception) {
                handleError(e, isInitial = false)
            }
        }
    }

    /**
     * Refreshes data with optimistic updates and error recovery
     */
    suspend fun refresh(
        loader: suspend (page: Int, size: Int) -> PaginationResult<T>
    ) {
        mutex.withLock {
            val current = _state.value
            _state.update {
                it.copy(
                    isRefreshing = true,
                    error = null,
                    lastRefreshTime =Clock.System.now().toEpochMilliseconds()
                )
            }

            try {
                val result = loader(1, current.pageSize)
                handleLoadResult(result, isInitial = true, isRefresh = true)
            } catch (e: Exception) {
                handleError(e, isRefresh = true)
            }
        }
    }

    /**
     * Retries the last failed operation with exponential backoff support
     */
    suspend fun retry(
        loader: suspend (page: Int, size: Int) -> PaginationResult<T>
    ) {
        mutex.withLock {
            val current = _state.value
            if (current.error == null || !current.error.isRecoverable) return@withLock

            _state.update { it.copy(error = null) }

            try {
                val page = if (current.items.isEmpty()) 1 else current.currentPage + 1
                val result = loader(page, current.pageSize)
                handleLoadResult(result, isInitial = current.items.isEmpty())
            } catch (e: Exception) {
                handleError(e, isInitial = current.items.isEmpty())
            }
        }
    }

    /**
     * Clears all data and resets state
     */
    fun clear() {
        _state.update { PaginationState() }
    }

    /**
     * Handles successful load results with proper state updates
     */
    private fun handleLoadResult(
        result: PaginationResult<T>,
        isInitial: Boolean,
        isRefresh: Boolean = false
    ) {
        _state.update { current ->
            val newItems = if (isInitial || isRefresh) {
                result.items
            } else {
                current.items + result.items
            }

            val newPage = if (isInitial || isRefresh) 1 else current.currentPage + 1
            val hasMore = result.hasMore && result.items.isNotEmpty()

            current.copy(
                items = newItems,
                currentPage = newPage,
                totalItems = result.totalItems,
                hasMore = hasMore,
                isInitialLoading = false,
                isLoadingMore = false,
                isRefreshing = false,
                error = null
            )
        }
    }

    /**
     * Centralized error handling with proper error classification
     */
    private fun handleError(
        exception: Exception,
        isInitial: Boolean = false,
        isRefresh: Boolean = false
    ) {
        val error = when (exception) {
             is IllegalArgumentException -> PaginationError.ValidationError(exception.message ?: "Invalid request")
            else -> {
                // Try to extract HTTP error codes if available
                val message = exception.message ?: "Unknown error occurred"
                if (message.contains("500")) {
                    PaginationError.ServerError(500, "Internal server error")
                } else if (message.contains("404")) {
                    PaginationError.ServerError(404, "Resource not found")
                } else {
                    PaginationError.UnknownError(message)
                }
            }
        }

        _state.update { current ->
            current.copy(
                isInitialLoading = false,
                isLoadingMore = false,
                isRefreshing = false,
                error = error
            )
        }
    }
}

/**
 * Result wrapper for pagination operations
 */
data class PaginationResult<T>(
    val items: List<T>,
    val totalItems: Int,
    val hasMore: Boolean
)
