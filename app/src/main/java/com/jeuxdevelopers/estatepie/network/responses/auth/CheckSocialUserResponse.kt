package com.jeuxdevelopers.estatepie.network.responses.auth

data class CheckSocialUserResponse(
    val `data`: Boolean = false,
    val message: String = "",
    val success: Boolean = false
)