package com.kashif.kmmnewsapp.domain.util


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
sealed class DataState<T>(
    val data: T? = null,
    val error: CustomMessages = CustomMessages.ExceptionMessage("Something Went Wrong")
) {
    class Success<T>(data: T?) : DataState<T>(data)
    class Loading<T> : DataState<T>()
    class Error<T>(customMessages: CustomMessages) : DataState<T>(error = customMessages)
    sealed class CustomMessages(val message: String = "") {

        object NoInternet : CustomMessages("No Internet")
        object NotFound : CustomMessages("Not Found")
        data class ExceptionMessage(val error: String) : CustomMessages(message = error)

    }
}
