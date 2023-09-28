package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class AllEventListResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val events: List<Event> = listOf(),
        val last_page: Int = 0,
        val next_page_url: Any = ""
    ){
        data class Event(
            val address: String = "",
            val date: String = "",
            val event_type: String = "",
            val id: Int = 0,
            val property_name: String = "",
            val status: String = "",
            val time: String = "",
            val title: String = ""
        )
    }
}