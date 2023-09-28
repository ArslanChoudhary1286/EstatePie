package com.jeuxdevelopers.estatepie.network.requests.tenant.auth

data class TenantSignupRequest(
    var email:String = "",
    var password:String = "",
    var name:String = "",
    var device_type:String = "",
    var password_confirmation:String = "",
    var device_token:String = "",
    var first_name:String = "",
    var last_name:String = "",
    var phone:String = "",

)
