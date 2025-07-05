package com.kashif.kmmnewsapp.core.pagination


sealed interface PaginationIntent {

    data object LoadInitial : PaginationIntent
    

    data object LoadMore : PaginationIntent
    

    data object Refresh : PaginationIntent
    

    data object Retry : PaginationIntent
    

    data object Clear : PaginationIntent
    
    /**
     * Set page size configuration
     */
    data class SetPageSize(val size: Int) : PaginationIntent
}
