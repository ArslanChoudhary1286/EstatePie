package com.jeuxdevelopers.estatepie.network.requests.management.billing

import java.io.File

data class SendInvoiceRequest(

    var amount: String = "",
    var date: String = "",
    var image: File? = null,
    var units: String = "",
    var property_id: String = "",
    var bill_type_id: String = "",
    var user_id: String = ""

)

