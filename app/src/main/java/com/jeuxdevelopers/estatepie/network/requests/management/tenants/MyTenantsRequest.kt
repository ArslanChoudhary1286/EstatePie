package com.jeuxdevelopers.estatepie.network.requests.management.tenants

data class MyTenantsRequest(
    var sort: String  = "",
    var keyword: String  = "",
    var search: String  = ""
)
