package com.jeuxdevelopers.estatepie.network.responses.management.tenants

data class AddNoteToTenantResponse(

    val `data`: Data= Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val id: Int = 0,
        val message: String = "",
        val property_id: Int = 0,
        val type: String = "",
        val user_id: Int = 0
    )
}