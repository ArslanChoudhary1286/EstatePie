package com.jeuxdevelopers.estatepie.network.responses.tenants.toggle

data class ToggleResponse(
    var `data`: Data = Data(),
    var message: String = "",
    var success: Boolean = false
) {
    data class Data(
        var address: Any = Any(),
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
        var phone: Any = Any()
    )
}