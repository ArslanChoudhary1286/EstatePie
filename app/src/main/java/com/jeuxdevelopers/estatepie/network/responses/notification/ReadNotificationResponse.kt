package com.jeuxdevelopers.estatepie.network.responses.notification

data class ReadNotificationResponse(
    val `data`: List<Any> = listOf(),
    val message: String = "",
    val success: Boolean = false
)