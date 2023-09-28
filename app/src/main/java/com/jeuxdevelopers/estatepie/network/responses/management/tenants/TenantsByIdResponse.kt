package com.jeuxdevelopers.estatepie.network.responses.management.tenants

data class TenantsByIdResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val description: String = "",
        val id: Int = 0,
        val image_url: String = "",
        val lease_info: String = "",
        val move_in_date: String = "",
        val name: String = "",
        val notes: List<Note> = listOf(),
        val phone: String = "",
        val property_id: Int = 0,
        val property_name: String = "",
        val vehicles: String = ""
    ){
        data class Note(
            val created_at: String = "",
            val id: Int = 0,
            val message: String = "",
            val property_id: Int = 0,
            val tenant_id: Int = 0,
            val type: String = "",
            val user_id: Int = 0
        )
    }
}