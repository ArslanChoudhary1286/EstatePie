package com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard

data class ViewBillDetailsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val amount: Int = 0,
        val bill_type: String = "",
        val bill_type_icon: String = "",
        val date: String = "",
        val id: Int = 0,
        val image: String = "",
        val status: String = "",
        val units: String = ""
    )
}