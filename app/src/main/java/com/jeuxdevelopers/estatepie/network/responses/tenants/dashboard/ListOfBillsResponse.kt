package com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard

data class ListOfBillsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val maintenance_requests: List<MaintenanceRequest> = listOf(),
        val next_page_url: String = ""
    ){
        data class MaintenanceRequest(
            val amount: Int = 0,
            val bill_type: String = "",
            val bill_type_icon: String = "",
            val date: String = "",
            val id: Int = 0,
            val status: String = "",
            val units: String = ""
        )
    }
}