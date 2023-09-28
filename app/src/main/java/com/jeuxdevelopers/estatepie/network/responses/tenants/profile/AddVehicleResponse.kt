package com.jeuxdevelopers.estatepie.network.responses.tenants.profile

data class AddVehicleResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val color: String = "",
        val created_at: String = "",
        val doors: String = "",
        val id: Int = 0,
        val licence_plate: String = "",
        val model: String = "",
        val registration_date: String = "",
        val updated_at: String = "",
        val vehicle_type: String = ""
    )
}