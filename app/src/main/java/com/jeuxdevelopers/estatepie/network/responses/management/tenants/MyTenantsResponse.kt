package com.jeuxdevelopers.estatepie.network.responses.management.tenants

data class MyTenantsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val next_page_url: String = "",
        val tenants: List<Tenant> = listOf()
    ){
        data class Tenant(
            val address: String = "",
            val id: Int = 0,
            val image: String = "",
            val name: String = ""
        )
    }
}