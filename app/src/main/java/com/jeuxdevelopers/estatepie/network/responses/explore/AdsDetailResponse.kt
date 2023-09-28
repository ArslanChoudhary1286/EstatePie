package com.jeuxdevelopers.estatepie.network.responses.explore

data class AdsDetailResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val address: String = "",
        val amenities: List<Amenity> = listOf(),
        val available: Int = 0,
        val bathroom: Int = 0,
        val beds: String = "",
        val broker: String = "",
        val category_id: Int = 0,
        val category_type: String = "",
        val description: String = "",
        val fees: String = "",
        val id: Int = 0,
        val image: String = "",
        val lease: Int = 0,
        val lease_terms: String = "",
        val listed_by: String = "",
        val multiunits: Int = 0,
        val name: String = "",
        val pets: String = "",
        val price: Int = 0,
        val property_images: List<PropertyImage> = listOf(),
        val property_type: String = "",
        val property_type_id: Int = 0,
        val purpose: String = "",
        val size: Int = 0,
        val smoking: String = "",
        val status: String = "",
        val unit: String = ""
    )

    data class Amenity(
        val icon: String = "",
        val id: Int = 0,
        val name: String = ""
    )

    data class PropertyImage(
        val ad_property_id: Int = 0,
        val id: Int = 0,
        val image: String = ""
    )
}