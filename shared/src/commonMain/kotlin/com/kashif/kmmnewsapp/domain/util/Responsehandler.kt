package com.kashif.kmmnewsapp.domain.util

enum class ErrorCodes(val code: Int) {
    NotFound(404)
}

open class ResponseHandler {

    fun <T : Any> handleException(): DataState<T> {
        return DataState.Error(DataState.CustomMessages.ExceptionMessage("No Internet."))
    }

    fun <T : Any>  handleException(exception: String): DataState<T> {
        return DataState.Error(DataState.CustomMessages.ExceptionMessage(exception))
    }

    fun <T : Any> handleSuccess(data: T?):DataState<T> {
        return DataState.Success(data)
    }
}

private fun getErrorType(code: Int): DataState.CustomMessages {
    return DataState.CustomMessages.ExceptionMessage("An error Occurred with code $code")
}


