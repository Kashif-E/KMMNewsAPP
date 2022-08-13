package com.kashif.kmmnewsapp.domain.util

enum class ErrorCodes(val code: Int) {
    NotFound(404)
}

open class ResponseHandler {

    fun <T : Any> handleException(): DataState<T> {
        return DataState.Error(DataState.CustomMessages.ExceptionMessage("No Internet."))
    }

    fun handleException(statusCode: Int): DataState.CustomMessages {
        return getErrorType(statusCode)
    }
}

private fun getErrorType(code: Int): DataState.CustomMessages {
  return DataState.CustomMessages.ExceptionMessage("An error Occurred with code $code")
}


