package com.jeuxdevelopers.estatepie.network.responses.management.properties

data class UpdateCommercialResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val apt_number: String = "",
        val category_id: Int = 0,
        val city: String = "",
        val created_at: String = "",
        val deleted_at: String = "",
        val description: String = "",
        val id: Int = 0,
        val is_active: Boolean = false,
        val lease_terms: String = "",
        val name: String = "",
        val notes: String = "",
        val property_type_id: Int = 0,
        val state: String = "",
        val status: String = "",
        val unit: String = "",
        val updated_at: String = "",
        val user_id: Int = 0,
        val zip: String = ""
    )
}