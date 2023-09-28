package com.jeuxdevelopers.estatepie.network.responses.management.tenants

data class AllTenantsResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val last_page: Int,
        val requests: List<Any>
    )
}