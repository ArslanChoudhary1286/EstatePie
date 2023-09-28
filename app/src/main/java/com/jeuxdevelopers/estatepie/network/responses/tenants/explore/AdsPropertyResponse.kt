package com.jeuxdevelopers.estatepie.network.responses.tenants.explore

data class AdsPropertyResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val ad_propreties: List<Any>? = listOf(),
        val last_page: Int = 0,
        val next_page_url: String = ""
    )
}