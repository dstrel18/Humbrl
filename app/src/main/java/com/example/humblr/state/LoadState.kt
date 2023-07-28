package com.example.humblr.state


sealed class LoadState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : LoadState<T>(data = data)

    class Error<T>(errorMessage: String) : LoadState<T>(message = errorMessage)

    class Loading<T> : LoadState<T>()

}
