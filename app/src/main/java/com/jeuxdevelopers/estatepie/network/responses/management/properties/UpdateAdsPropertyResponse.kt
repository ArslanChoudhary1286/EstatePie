package com.jeuxdevelopers.estatepie.network.responses.management.properties

data class UpdateAdsPropertyResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
//        val ad_properties.property_type_id: String,
        val address: String = "",
        val available: String = "",
        val bathroom: Int = 0,
        val beds: String = "",
        val broker: String = "",
        val category_id: Int = 0,
        val created_at: String = "",
        val deleted_at: String = "",
        val description: String = "",
        val fees: String = "",
        val id: Int = 0,
        val is_active: Boolean = false,
        val latitude: String = "",
        val lease: Int = 0,
        val lease_terms: String = "",
        val longitude: String = "",
        val multiunits: String = "",
        val name: String = "",
        val pets: String = "",
        val price: Int = 0,
        val property_type_id: String = "",
        val purpose: String = "",
        val size: Int = 0,
        val smoking: Boolean = false,
        val status: String = "",
        val unit: String = "",
        val updated_at: String = "",
        val user_id: Int = 0
    )
}