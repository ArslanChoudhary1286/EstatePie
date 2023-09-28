package com.jeuxdevelopers.estatepie.network.requests.tenant.billing

data class UpdatePaymentScheduleRequest(
    var title: String = "",
    var amount: String = "0",
    var pick_date: String = "",
    var _method: String = "put",
)
