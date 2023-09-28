package com.jeuxdevelopers.estatepie.network.requests.management.properties

import java.io.File

data class UpdateAdsRequest(

    var id: Int  = 0,
    var name: String  = "",
    var category: String  = "",
    var purpose: String  = "",
    var unit: String  = "",
    var size: String  = "",
    var broker: String  = "",
    var price: String  = "",
    var address: String  = "",
    var description: String  = "",
    var lease: String  = "",
    var beds: String  = "",
    var fees: String  = "",
    var latitude: String  = "",
    var longitude: String  = "",
    var multiunits: String  = "",
    var available: String  = "",
    var images: MutableList<File> = mutableListOf(),
    //   amenities separted by comma 4,3
    var amenities: String  = "",
    var property_type: String  = "",
    var lease_terms: String  = "",
    var _method: String  = "put"

)
