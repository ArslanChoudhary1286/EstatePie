package com.jeuxdevelopers.estatepie.network.responses.explore

data class LeaseTermResponse(
    val data: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val content: String = "",
        val created_at: String = "",
        val id: Int = 0,
        val slug: String = "",
        val title: String = ""
    )
}