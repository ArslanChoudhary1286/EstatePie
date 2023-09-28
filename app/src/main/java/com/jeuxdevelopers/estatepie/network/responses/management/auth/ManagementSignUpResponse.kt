package com.jeuxdevelopers.estatepie.network.responses.management.auth

data class ManagementSignUpResponse(
    var data: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        var user: User = User()
    ){
        data class User(
            val access_token: String = "",
            val card_brand: Any = Any(),
            val card_last_four: Any = Any(),
            val card_month: Any = Any(),
            val card_year: Any = Any(),
            val created_at: String = "",
            var details: Details = Details(),
            val email: String = "",
            val expires_in: Int = 0,
            val id: Int = 0,
            val is_verify: String = "",
            val name: String = "",
            val role_id: Int = 0,
            val token_type: String = "",
            val trial_ends_at: String = "",
            val user_plan: Int = 0
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
}