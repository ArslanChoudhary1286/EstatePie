package com.jeuxdevelopers.estatepie.network.responses.plans

data class CreateCustomerResponse(
    val `data`: Data= Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val card_brand: String = "",
        val card_last_four: String = "",
        val card_month: String = "",
        val card_year: String = "",
        val id: Int = 0
    )
}