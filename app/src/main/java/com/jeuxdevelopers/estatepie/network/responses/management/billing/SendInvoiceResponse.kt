package com.jeuxdevelopers.estatepie.network.responses.management.billing

data class SendInvoiceResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val amount: Int = 0,
        val bill_type_id: Int = 0,
        val created_at: String = "",
        val date: String = "",
        val id: Int = 0,
        val image: String = "",
        val property_id: Int = 0,
        val status: String = "",
        val units: String = "",
        val updated_at: String = "",
        val user_id: Int = 0
    )
}