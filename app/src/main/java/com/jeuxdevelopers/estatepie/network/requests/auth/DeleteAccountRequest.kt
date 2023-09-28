package com.jeuxdevelopers.estatepie.network.requests.auth

data class DeleteAccountRequest(
    var device_type:String = "",
    var device_token:String = ""
)
