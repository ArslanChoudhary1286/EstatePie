package com.jeuxdevelopers.estatepie.network.responses.explore

data class MarkToFavoriteResponse(
    val `data`: List<Any>? = null,
    val message: String,
    val success: Boolean = false
)