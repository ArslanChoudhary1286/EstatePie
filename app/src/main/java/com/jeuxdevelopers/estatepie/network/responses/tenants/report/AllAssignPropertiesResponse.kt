package com.jeuxdevelopers.estatepie.network.responses.tenants.report

data class AllAssignPropertiesResponse(
    val `data`: List<Data> = listOf(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val id: Int = 0,
        val name: String = ""
    )
}