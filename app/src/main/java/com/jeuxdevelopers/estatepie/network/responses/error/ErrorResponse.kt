package com.jeuxdevelopers.estatepie.network.responses.error

data class ErrorResponse(
    val `data`: List<Any> = listOf(),
    val errors: List<Error> = listOf(),
    val message: String = "",
    val success: Boolean = false
){
    data class Error(
        val label: String = "",
        val message: String = ""
    )
}