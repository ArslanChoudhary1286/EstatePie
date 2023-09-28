package com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard

data class MaintenanceRequestResponse(
    val `data`: Data = Data(),
    val message: String  = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val maintenance_requests: List<MaintenanceRequest> = listOf(),
        val next_page_url: String = ""
    ){
        data class MaintenanceRequest(
            val comments: List<Any> = listOf(),
            val created_at: String = "",
            val description: String = "",
            val id: Int = 0,
            val images: List<Image> = listOf(),
            val property_image: Any = "",
            val property_name: Any = "",
            val status: String = "",
            val title: String = "",
            val type: String = ""
        ){
            data class Image(
                val id: Int = 0,
                val image: String = "",
                val request_id: Int = 0
            )
        }
    }
}