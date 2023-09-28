package com.jeuxdevelopers.estatepie.network.responses.tenants.billing

data class AddPaymentScheduleResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val id: Int = 0,
        val pick_date: String = "",
        val title: String = "",
        val user_id: Int = 0
    )
}