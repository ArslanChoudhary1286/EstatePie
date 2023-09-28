package com.jeuxdevelopers.estatepie.network.requests.tenant.billing

data class AddPaymentScheduleRequest(
    var title: String = "",
    var amount: String = "0",
    var pick_date: String = "",
    var is_schedule: Int = 0,
)
