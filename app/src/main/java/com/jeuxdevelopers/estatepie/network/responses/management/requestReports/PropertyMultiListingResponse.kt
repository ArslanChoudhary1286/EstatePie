package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class PropertyMultiListingResponse(
    val `data`: List<Data> = listOf(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val id: Int = 0,
        val name: String = ""
    )
}