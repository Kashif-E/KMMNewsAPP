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


        object NetworkError : CustomMessages("Something wrong with network, please try again.")
        object RandomError : CustomMessages("Something went wrong, please try again.")
        object ResponseError :
            CustomMessages("We are fixing your problem, Thank you for your patience.")

        object NoInternet : CustomMessages("No Internet")
        object NotFound : CustomMessages("Not Found")
        data class ExceptionMessage(val error: String) : CustomMessages(message = error)

    }
}
