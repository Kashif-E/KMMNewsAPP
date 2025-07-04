package com.kashif.kmmnewsapp.core.pagination

/**
 * Clean MVI intents for pagination following research best practices.
 * 
 * These intents provide clear separation of user intentions and support
 * comprehensive testing and state management.
 */
sealed interface PaginationIntent {
    /**
     * Load initial data (first page)
     */
    data object LoadInitial : PaginationIntent
    
    /**
     * Load next page of data
     */
    data object LoadMore : PaginationIntent
    
    /**
     * Refresh all data (pull-to-refresh)
     */
    data object Refresh : PaginationIntent
    
    /**
     * Retry last failed operation
     */
    data object Retry : PaginationIntent
    
    /**
     * Clear all data
     */
    data object Clear : PaginationIntent
    
    /**
     * Set page size configuration
     */
    data class SetPageSize(val size: Int) : PaginationIntent
}
