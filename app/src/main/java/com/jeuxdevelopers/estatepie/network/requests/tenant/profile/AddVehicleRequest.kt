package com.jeuxdevelopers.estatepie.network.requests.tenant.profile

import java.io.File

data class AddVehicleRequest(
    var vehicle_type: String = "",
    var model: String = "",
    var doors: String = "0",
    var color: String = "",
    var registration_date: String = "",
    var licence_plate: String = "",
    var images: MutableList<File> = mutableListOf(),
)
