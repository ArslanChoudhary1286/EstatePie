package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

data class IncidentWithFilterDetailRequest(

    var id: String = "",
    var keyword: String = "",
    var date: String = "",
    var status: String = ""
)
