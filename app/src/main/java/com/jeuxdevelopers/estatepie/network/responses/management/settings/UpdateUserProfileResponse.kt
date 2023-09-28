package com.jeuxdevelopers.estatepie.network.responses.management.settings

data class UpdateUserProfileResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val card_brand: Any = Any(),
        val card_last_four: Any = Any(),
        val card_month: Any = Any(),
        val card_year: Any = Any(),
        val created_at: String = "",
        val details: Details = Details(),
        val email: String = "",
        val id: Int = 0,
        val is_verify: Any = Any(),
        val name: String = "",
        val role_id: Int = 0,
        val trial_ends_at: String = ""
    ){
        data class Details(
            val address: String = "",
            val business_name: String = "",
            val email_updates: Int = 0,
            val first_name: String = "",
            val id: Int = 0,
            val image: String = "",
            val image_url: String = "",
            val is_social_login: Int = 0,
            val is_verified: Int = 0,
            val last_name: String = "",
            val notifications: Int = 0,
            val phone: String = ""
        )
    }

}