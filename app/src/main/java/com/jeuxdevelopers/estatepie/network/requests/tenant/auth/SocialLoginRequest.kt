package com.jeuxdevelopers.estatepie.network.requests.tenant.auth

data class SocialLoginRequest(

    var platform:String = "",
    var client_id:String = "",
    var token:String = "",
    var username:String = "",
    var email:String = "",
    var device_token:String = "",
    var device_type:String = "",
    var business_name:String = "",
    var role_id:Int = 0

)
