package com.jeuxdevelopers.estatepie.network.responses.plans

data class PayBillResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val amount: Int,
        val bill_type_id: Int,
        val created_at: String,
        val date: String,
        val deleted_at: Any,
        val id: Int,
        val image: String,
        val paid_on: Any,
        val property_id: Int,
        val status: String,
        val units: String,
        val updated_at: String,
        val user_id: Int
    )
}