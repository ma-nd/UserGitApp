package com.example.userrepo.base.data

sealed class Response<out T : Any> {
    data class Success<out T : Any>(val data: T) : Response<T>()
    data class Error(val exception: Throwable) : Response<Nothing>()

    inline fun <T : Any> Response<T>.onSuccess(action: (Any) -> Unit): Response<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun <T : Any> Response<T>.onError(action: (Error) -> Unit): Response<T> {
        if (this is Error) action(this)
        return this
    }
}