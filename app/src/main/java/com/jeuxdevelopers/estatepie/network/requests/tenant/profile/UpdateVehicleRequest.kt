package com.jeuxdevelopers.estatepie.network.requests.tenant.profile

import java.io.File

data class UpdateVehicleRequest(
    var id : Int = 0,
    var vehicle_type: String = "",
    var model: String = "",
    var doors: String = "",
    var color: String = "",
    var registration_date: String = "",
    var licence_plate: String = "",
    var images: MutableList<File> = mutableListOf(),
    var _method : String = "put"
)
