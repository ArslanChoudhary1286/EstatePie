package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class EventsDetailResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val date: String = "",
        val description: String = "",
        val event_type: String = "",
        val id: Int = 0,
        val images: List<Image> = listOf(),
        val latitude: String = "",
        val longitude: String = "",
        val property_name: String = "",
        val time: String = "",
        val title: String = ""
    ){
        data class Image(
            val event_id: Int = 0,
            val id: Int = 0,
            val image: String = ""
        )
    }
}