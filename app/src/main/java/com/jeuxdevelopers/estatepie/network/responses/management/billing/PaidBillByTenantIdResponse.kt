package com.jeuxdevelopers.estatepie.network.responses.management.billing

data class PaidBillByTenantIdResponse(
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
            val amount: Int = 0,
            val bill_type: String = "",
            val bill_type_icon: String = "",
            val date: String = "",
            val id: Int = 0,
            val image: String = "",
            val property_id: Int = 0,
            val property_name: String = "",
            val status: String = ""
        )
    }
}