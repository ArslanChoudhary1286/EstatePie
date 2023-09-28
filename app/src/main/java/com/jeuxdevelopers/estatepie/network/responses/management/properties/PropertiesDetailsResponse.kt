package com.jeuxdevelopers.estatepie.network.responses.management.properties

data class PropertiesDetailsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val apt_number: String = "",
        val category_id: Int = 0,
        val category_type: String = "",
        val created_at: String = "",
        val created_on: String = "",
        val description: String = "",
        val id: Int = 0,
        val name: String = "",
        val notes: List<Note> = listOf(),
        val property_assign_email: String = "",
        val property_assign_id: String = "",
        val property_assign_phone: String = "",
        val property_assign_to: String = "",
        val property_images: List<PropertyImage> = listOf(),
        val share_link: String = "",
        val status: String = "",
        val unit: String = ""
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

        data class PropertyImage(
            val id: Int = 0,
            val image: String = "",
            val property_id: Int = 0
        )
    }

}