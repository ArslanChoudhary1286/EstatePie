package com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard

data class IncidentReportResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val incident_requests: List<IncidentRequest> = listOf(),
        val last_page: Int = 0,
        val next_page_url: String = ""
    ){
        data class IncidentRequest(
            val comments: List<Any> = listOf(),
            val created_at: String = "",
            val description: String = "",
            val id: Int = 0,
            val images: List<Image> = listOf(),
            val property_image: Any = "",
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