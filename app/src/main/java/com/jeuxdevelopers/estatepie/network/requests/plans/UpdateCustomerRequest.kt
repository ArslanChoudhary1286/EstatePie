package com.jeuxdevelopers.estatepie.network.requests.plans

data class UpdateCustomerRequest(
    var card_name: String = "",
    var card_no: String = "",
    var card_exp: String = "",
    var card_cvv: String = "",
)
