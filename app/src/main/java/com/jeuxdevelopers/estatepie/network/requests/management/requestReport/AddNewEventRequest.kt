package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

import java.io.File

data class AddNewEventRequest(

    var title: String  = "",
    var date: String  = "",
    var description: String  = "",
    var address: String  = "",
    var latitude: String  = "",
    var longitude: String  = "",
    var images: MutableList<File> = mutableListOf(),
    var event_type_id: String  = "",
    var property_id: String  = "",
    var time: String  = "",

)
