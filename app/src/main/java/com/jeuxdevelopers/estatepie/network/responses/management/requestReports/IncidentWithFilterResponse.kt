package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class IncidentWithFilterResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val incident_request: List<IncidentRequest> = listOf(),
        val last_page: Int = 0,
        val next_page_url: Any = "",
        val resolved: Int = 0,
        val under_review: Int = 0
    ){
        data class IncidentRequest(
            val created_at: String = "",
            val description: String = "",
            val id: Int = 0,
            val image: String = "",
            val images: List<Image> = listOf(),
            val name: String = "",
            val property_id: Int = 0,
            val property_name: String = "",
            val status: String = "",
            val title: String = ""
        ){
            data class Image(
                val id: Int = 0,
                val image: String = "",
                val request_id: Int = 0
            )
        }
    }
}