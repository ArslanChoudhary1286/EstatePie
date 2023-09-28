package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class ChangeRequestStatusResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val created_at: String = "",
        val deleted_at: Any = "",
        val description: String = "",
        val id: Int = 0,
        val is_complete: String = "",
        val property_id: Int = 0,
        val request_type_id: Int = 0,
        val title: String = "",
        val updated_at: String = "",
        val user_id: Int = 0
    )
}