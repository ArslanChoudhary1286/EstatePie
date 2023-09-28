package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class UploadCSVResponse(
    val `data`: Boolean = false,
    val message: String = "",
    val success: Boolean = false
)