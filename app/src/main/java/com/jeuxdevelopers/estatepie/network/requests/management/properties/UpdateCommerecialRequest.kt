package com.jeuxdevelopers.estatepie.network.requests.management.properties

import java.io.File

data class UpdateCommerecialRequest(

    var id: Int  = 0,
    var name: String  = "",
    var category_id: String  = "",
    var unit: String  = "",
    var apt_number: String  = "",
//    var number_range_from: String  = "",
//    var number_range_to: String  = "",
//    var range_alpha_from: String  = "",
//    var range_alpha_to: String  = "",
    var address: String  = "",
    var latitude: String  = "",
    var longitude: String  = "",
    var notes: String  = "",
    var _method: String  = "put",
//    var status: String  = "",
    var images: MutableList<File> = mutableListOf()

)
