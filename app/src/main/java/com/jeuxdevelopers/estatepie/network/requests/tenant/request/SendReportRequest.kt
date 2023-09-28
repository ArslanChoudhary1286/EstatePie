package com.jeuxdevelopers.estatepie.network.requests.tenant.request

import java.io.File

data class SendReportRequest(

    var request_type_id: String = "",
    var property_id: String = "",
    var title: String = "",
    var description: String = "",
    var images: MutableList<File> = mutableListOf()

)
