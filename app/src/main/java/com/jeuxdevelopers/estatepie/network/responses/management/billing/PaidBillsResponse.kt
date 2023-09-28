package com.jeuxdevelopers.estatepie.network.responses.management.billing

data class PaidBillsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val bills: List<Bill> = listOf(),
        val last_page: Int = 0,
        val next_page_url: String = ""
    ){
        data class Bill(
            val address: String = "",
            val id: Int = 0,
            val image: String = "",
            val name: String = ""
        )
    }
}