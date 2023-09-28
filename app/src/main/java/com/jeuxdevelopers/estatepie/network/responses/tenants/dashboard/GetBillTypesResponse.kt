package com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard

data class GetBillTypesResponse(
    val `data`: List<Data> = listOf(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val icon: String = "",
        val id: Int = 0,
        val name: String = ""
    )
}