package com.jeuxdevelopers.estatepie.network.responses.tenants.billing

data class PaymentHistoryResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val bills: List<Any> = listOf(),
        val last_page: Int = 0
    )
}