package com.jeuxdevelopers.estatepie.network.responses.tenants.auth

import com.jeuxdevelopers.estatepie.network.responses.error.ErrorResponse

data class TenantSignupResponse(
    var data: Data = Data(),
    var message: String = "",
    var success: Boolean = false,
//    val errors: List<ErrorResponse.Error> = listOf()
) {
    data class Data(
        var user: User = User()
    ) {
        data class User(
            var access_token: String = "",
            var card_brand: Any = Any(),
            var card_last_four: Any = Any(),
            var card_month: Any = Any(),
            var card_year: Any = Any(),
            var created_at: String = "",
            var details: Details = Details(),
            var email: String = "",
            var expires_in: Int = 0,
            var id: Int = 0,
            var is_verify: String = "",
            var name: String = "",
            var role_id: Int = 0,
            var token_type: String = "",
            var trial_ends_at: String = "",
            var user_plan: Int = 0
        ) {
            data class Details(
                var address: String = "",
                var business_name: String = "",
                var email_updates: Int = 0,
                var first_name: String = "",
                var id: Int = 0,
                var image: String = "",
                var image_url: String = "",
                var is_social_login: Int = 0,
                var is_verified: Int = 0,
                var last_name: String = "",
                var notifications: Int = 0,
                var phone: String = ""
            )
        }
    }

//    data class Error(
//        val label: String = "",
//        val message: String = ""
//    )
}