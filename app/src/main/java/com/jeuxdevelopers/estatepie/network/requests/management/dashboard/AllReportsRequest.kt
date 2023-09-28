package com.jeuxdevelopers.estatepie.network.requests.management.dashboard

data class AllReportsRequest(
    var page : String = "1",
    var report_type: String = "",
    var date: String = ""
)
