package com.jeuxdevelopers.estatepie.network.requests.tenant.auth

data class CheckSocialUserRequest(

    var platform:String = "",
    var client_id:String = ""

)
