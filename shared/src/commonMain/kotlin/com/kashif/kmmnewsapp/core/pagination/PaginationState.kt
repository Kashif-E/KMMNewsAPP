package com.kashif.kmmnewsapp.core.pagination


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

    val accessibilityDescription: String
        get() = when {
            isInitialLoading -> "Loading content, please wait"
            isRefreshing -> "Refreshing content, please wait"
            isLoadingMore -> "Loading more items, ${items.size} of $totalItems loaded"
            error != null -> "Error loading content: ${error.message}. Retry available."
            items.isEmpty() -> "No items available"
            else -> "${items.size} of $totalItems items loaded"
        }


    fun shouldLoadMore(lastVisibleIndex: Int, bufferSize: Int = 5): Boolean {
        return hasMore && 
               !isLoadingMore && 
               !isInitialLoading && 
               error == null &&
               items.isNotEmpty() &&
               lastVisibleIndex >= (items.size - bufferSize)
    }


    val canLoadMore: Boolean
        get() = hasMore && !isLoadingMore && !isInitialLoading && error == null


    val loadingProgress: Float
        get() = if (totalItems > 0) items.size.toFloat() / totalItems else 0f
}


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
