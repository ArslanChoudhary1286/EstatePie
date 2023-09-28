package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

data class AllEventListRequest(
    var keyword: String = "",
    var date: String = "",
    var status: String = ""
)
