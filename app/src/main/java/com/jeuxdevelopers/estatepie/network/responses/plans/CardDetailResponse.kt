package com.jeuxdevelopers.estatepie.network.responses.plans

data class CardDetailResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val card_brand: String,
        val card_last_four: String,
        val card_month: String,
        val card_year: String,
        val id: Int
    )
}