package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

data class MaintenanceWithFilterRequest(
    var keyword: String = "",
    var date: String = "",
    var status: String = ""
)
