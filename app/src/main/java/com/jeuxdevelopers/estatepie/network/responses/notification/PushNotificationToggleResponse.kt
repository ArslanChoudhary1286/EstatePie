package com.jeuxdevelopers.estatepie.network.responses.notification

data class PushNotificationToggleResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val business_name: String = "",
        val email_updates: Int = 0,
        val first_name: String = "",
        val id: Int = 0,
        val image: Any = Any(),
        val image_url: String = "",
        val is_social_login: Int = 0,
        val is_verified: Int = 0,
        val last_name: String = "",
        val notifications: Int = 0,
        val phone: String = ""
    )
}