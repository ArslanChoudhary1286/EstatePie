package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class MaintenanceWithFilterDetailResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val comments: List<Comment> = listOf(),
        val created_at: String = "",
        val description: String = "",
        val id: Int = 0,
        val image: String = "",
        val images: List<Image> = listOf(),
        val is_complete: Int = 0,
        val name: String = "",
        val property_image: String = "",
        val property_name: String = "",
        val property_unit: String = "",
        val title: String = ""
    ){
        data class Comment(
            val comments: String = "",
            val created_at: String = "",
            val deleted_at: Any = "",
            val id: Int = 0,
            val request_id: Int = 0,
            val updated_at: String = ""
        )

        data class Image(
            val id: Int = 0,
            val image: String = "",
            val request_id: Int = 0
        )
    }
}