package com.jeuxdevelopers.estatepie.network.responses.management.requestReports

data class PostCommentResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val comments: String = "",
        val created_at: String = "",
        val id: Int = 0,
        val request_id: String = "",
        val updated_at: String = ""
    )
}