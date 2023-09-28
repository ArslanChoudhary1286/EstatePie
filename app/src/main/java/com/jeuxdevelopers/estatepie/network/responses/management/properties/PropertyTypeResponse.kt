package com.jeuxdevelopers.estatepie.network.responses.management.properties

data class PropertyTypeResponse(
    val `data`: List<Data> = listOf(),
    val max: Int = 0,
    val message: String = "",
    val min: Int = 0,
    val success: Boolean = false
){
    data class Data(
        val category_id: Int = 0,
        val id: Int = 0,
        val name: String = ""
    )
}