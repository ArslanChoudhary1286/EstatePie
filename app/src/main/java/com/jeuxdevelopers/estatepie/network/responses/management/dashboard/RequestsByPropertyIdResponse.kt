package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class RequestsByPropertyIdResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val requests: List<Request> = listOf()
    ){
        data class Request(
            val created_at: String = "",
            val description: String = "",
            val id: Int = 0,
            val image: String = "",
            val name: String = "",
            val status: String = ""
        )
    }
}