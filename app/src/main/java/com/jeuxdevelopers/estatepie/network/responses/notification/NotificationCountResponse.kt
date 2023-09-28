package com.jeuxdevelopers.estatepie.network.responses.notification

data class NotificationCountResponse(
    val `data`: Int,
    val message: String,
    val success: Boolean
)