package com.jeuxdevelopers.estatepie.utils

sealed class Response<out T> {

    object Loading : Response<Nothing>()

    data class Success<T>(val success: T) : Response<T>()

    data class Error(val error: String?) : Response<Nothing>()

}
