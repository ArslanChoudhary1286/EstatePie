package com.jeuxdevelopers.estatepie.network.requests.tenant.auth

data class TenantLoginRequest(
    var email:String = "",
    var password:String = "",
    var device_type:String = "",

)
