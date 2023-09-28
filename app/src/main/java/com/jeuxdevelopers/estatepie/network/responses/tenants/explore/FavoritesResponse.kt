package com.jeuxdevelopers.estatepie.network.responses.tenants.explore

data class FavoritesResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val next_page_url: String = "",
        val recommended: List<Recommended> = listOf()
    ){
        data class Recommended(
            val address: String = "",
            val amenities: List<Any>? = null,
            val available: Int = 0,
            val beds: String = "",
            val broker: String = "",
            val category_id: Int = 0,
            val category_type: String = "",
            val created_on: String = "",
            val description: String = "",
            val fees: String = "",
            val id: Int = 0,
            val image: String = "",
            val is_favourite: Int = 0,
            val lease: String = "",
            val lease_terms: String = "",
            val listed_by: String = "",
            val listed_id: Int = 0,
            val listed_image: String = "",
            val multiunits: Int = 0,
            val name: String = "",
            val price: Int = 0,
            val property_images: List<PropertyImage> = listOf(),
            val property_type: String = "",
            val property_type_id: Int = 0,
            val purpose: String = "",
            val size: Int = 0,
            val status: String = "",
            val unit: String = ""
        ){
            data class PropertyImage(
                val ad_property_id: Int = 0,
                val id: Int = 0,
                val image: String = ""
            )
        }
    }
}