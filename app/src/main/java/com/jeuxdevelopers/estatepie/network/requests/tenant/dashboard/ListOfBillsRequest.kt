package com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard

data class ListOfBillsRequest(
    var bill_type_id : String = "",
    var status : String = ""
)
