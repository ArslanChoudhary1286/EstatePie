package com.jeuxdevelopers.estatepie.network.responses.management.settings

data class UserProfileResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val email: String = "",
        val id: Int = 0,
        val image: String = "",
        val name: String = "",
        val phone: String = "",
        val property_id: Int = 0,
        val property_name: String = "",
        val property_unit: String = ""
    )
}