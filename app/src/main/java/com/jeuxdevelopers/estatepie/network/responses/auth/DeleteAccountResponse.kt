package com.jeuxdevelopers.estatepie.network.responses.auth

data class DeleteAccountResponse(
    val `data`: List<Any> = listOf(),
    val message: String = "",
    val success: Boolean = false
)