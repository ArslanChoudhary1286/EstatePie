package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class AddNewEventResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val created_at: String = "",
        val date: String = "",
        val description: String  = "",
        val event_type_id: Int = 0,
        val id: Int = 0,
        val latitude: String = "",
        val longitude: String = "",
        val property_id: Int = 0,
        val time: String = "",
        val title: String = "",
        val updated_at: String = "",
        val user_id: Int = 0
    )
}