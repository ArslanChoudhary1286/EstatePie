package com.jeuxdevelopers.estatepie.network.responses.tenants.profile

data class GetVehicleResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val next_page_url: String = "",
        val vehicles: List<Vehicle> = listOf()
    ){
        data class Vehicle(
            val color: String = "",
            val doors: String = "",
            val id: Int = 0,
            val licence_plate: String = "",
            val model: String = "",
            val registration_date: String = "",
            val vehicle_images: List<VehicleImage> = listOf(),
            val vehicle_type: String = ""
        ){
            data class VehicleImage(
                val id: Int = 0,
                val image: String = "",
                val vehicle_id: Int = 0
            )
        }
    }
}