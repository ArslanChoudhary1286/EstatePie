package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

data class IncidentWithFilterRequest(
    var keyword: String = "",
    var date: String = "",
    var status: String = "",
    var page: String = ""
)
