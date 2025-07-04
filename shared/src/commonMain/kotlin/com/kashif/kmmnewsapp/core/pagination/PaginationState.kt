package com.kashif.kmmnewsapp.core.pagination

/**
 * Production-ready pagination state with comprehensive error handling and accessibility support.
 * 
 * This state model follows the research findings for building maintainable pagination systems:
 * - Separates different types of loading states
 * - Handles error recovery mechanisms
 * - Provides accessibility context
 * - Supports efficient recomposition
 */
data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 1,
    val pageSize: Int = 20,
    val totalItems: Int = 0,
    val isInitialLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: PaginationError? = null,
    val hasMore: Boolean = true,
    val lastRefreshTime: Long = 0L
) {
    /**
     * Computed property for accessibility - provides screen readers with context
     */
    val accessibilityDescription: String
        get() = when {
            isInitialLoading -> "Loading content, please wait"
            isRefreshing -> "Refreshing content, please wait"
            isLoadingMore -> "Loading more items, ${items.size} of $totalItems loaded"
            error != null -> "Error loading content: ${error.message}. Retry available."
            items.isEmpty() -> "No items available"
            else -> "${items.size} of $totalItems items loaded"
        }

    /**
     * Determines if we're near the end and should trigger loading more
     */
    fun shouldLoadMore(lastVisibleIndex: Int, bufferSize: Int = 5): Boolean {
        return hasMore && 
               !isLoadingMore && 
               !isInitialLoading && 
               error == null &&
               items.isNotEmpty() &&
               lastVisibleIndex >= (items.size - bufferSize)
    }

    /**
     * Check if pagination can be triggered (prevents duplicate requests)
     */
    val canLoadMore: Boolean
        get() = hasMore && !isLoadingMore && !isInitialLoading && error == null

    /**
     * Provides loading progress for accessibility
     */
    val loadingProgress: Float
        get() = if (totalItems > 0) items.size.toFloat() / totalItems else 0f
}

/**
 * Comprehensive error types for different pagination scenarios
 */
sealed class PaginationError(val message: String, val isRecoverable: Boolean = true) {
    data class NetworkError(val cause: String) : PaginationError(
        message = "Network error: $cause",
        isRecoverable = true
    )
    
    data class ServerError(val code: Int, val cause: String) : PaginationError(
        message = "Server error ($code): $cause",
        isRecoverable = true
    )
    
    data class ValidationError(val cause: String) : PaginationError(
        message = "Validation error: $cause",
        isRecoverable = false
    )
    
    data class UnknownError(val cause: String) : PaginationError(
        message = "Unknown error: $cause",
        isRecoverable = true
    )
}
