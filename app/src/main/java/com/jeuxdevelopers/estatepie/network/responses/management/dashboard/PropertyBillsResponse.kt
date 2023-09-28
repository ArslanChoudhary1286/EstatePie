package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class PropertyBillsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val bills: List<Bill> = listOf(),
        val last_page: Int = 0
    ){
        data class Bill(
            val amount: Int = 0,
            val bill_type: String = "",
            val bill_type_icon: String = "",
            val bill_type_id: Int = 0,
            val date: String = "",
            val id: Int = 0,
            val image: String = "",
            val paid_on: String = "",
            val property_id: Int = 0,
            val property_name: String = "",
            val status: String = "",
            val units: String = "",
            val user_id: Int = 0
        )
    }
}