package com.jeuxdevelopers.estatepie.sealed

sealed class NetworkResult<T>(
    val result: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
//    class ErrorResponse<T>(error: T) : NetworkResult<T>(error)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
    class Idle<T> : NetworkResult<T>()
}