package com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard

data class MaintenanceRequestRequest(
    var request_type_id : String = "",
    var status : String = "",
    var date : String = ""
)
