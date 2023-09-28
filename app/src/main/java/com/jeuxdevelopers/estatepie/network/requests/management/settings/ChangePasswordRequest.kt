package com.jeuxdevelopers.estatepie.network.requests.management.settings

data class ChangePasswordRequest(

    var current_password: String  = "",
    var password: String  = "",
    var password_confirmation: String  = ""

)
