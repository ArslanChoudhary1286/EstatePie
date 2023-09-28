package com.jeuxdevelopers.estatepie.network.responses.management.csv

data class GetSampleCSVResponse(
    val `data`: List<Data> = listOf(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val `file`: String = "",
        val id: Int = 0,
        val name: String = ""
    )
}