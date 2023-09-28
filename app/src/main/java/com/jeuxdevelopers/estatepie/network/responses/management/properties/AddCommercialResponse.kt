package com.jeuxdevelopers.estatepie.network.responses.management.properties

data class AddCommercialResponse(
    val `data`: List<Any> = listOf(),
    val message: String = "",
    val success: Boolean = false
)