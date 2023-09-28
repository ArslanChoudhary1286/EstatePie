package com.jeuxdevelopers.estatepie.network.responses.notification

data class GetNotificationsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val notifications: List<Notification> = listOf()
    ){
        data class Notification(
            val action_type: Int = 0,
            val created_at: String = "",
            val id: String = "",
            val image: String = "",
            val is_read: Int = 0,
            val message: String = "",
            val ref_id: Int = 0,
            val sender_id: Int = 0,
            val url: String = ""
        )
    }
}