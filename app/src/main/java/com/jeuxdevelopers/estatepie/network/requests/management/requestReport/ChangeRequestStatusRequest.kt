package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

data class ChangeRequestStatusRequest(
    var id : String = "",
    var keyword: String = "",
    var date: String = "",
    var status: String = ""
)
