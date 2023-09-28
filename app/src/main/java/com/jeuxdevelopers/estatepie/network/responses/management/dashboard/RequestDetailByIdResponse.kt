package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class RequestDetailByIdResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val comments: List<Any> = listOf(),
        val created_at: String = "",
        val description: String = "",
        val images: List<Image> = listOf(),
        val status: String = "",
        val title: String =  "",
        val type: String = ""
    ){
        data class Image(
            val id: Int = 0,
            val image: String = "",
            val request_id: Int = 0
        )
    }
}