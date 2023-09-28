package com.jeuxdevelopers.estatepie.network.responses.management.properties

data class CommercialPropertiesResponse(
    val `data`: Data =  Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val next_page_url: String = "",
        val propreties: List<Proprety> = listOf()
    ){
        data class Proprety(
            val address: String = "",
            val apt_number: String = "",
            val category_id: Int = 0,
            val category_type: String = "",
            val created_at: String = "",
            val created_on: String = "",
            val id: Int = 0,
            val image: String = "",
            val name: String = "",
            val notes: String = "",
            val property_images: List<PropertyImage> = listOf(),
            val status: String = "",
            val unit: String = ""
        ){
            data class PropertyImage(
                val id: Int = 0,
                val image: String = "",
                val property_id: Int = 0
            )
        }
    }
}