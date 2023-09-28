package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class AllReportsResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val requests: List<Request> = listOf()
    ){
        data class Request(
            val address: String = "",
            val completed: Int = 0,
            val image: String = "",
            val name: String = "",
            val percentage: String = "",
            val property_id: Int = 0,
            val remaining: Int = 0,
            val status1: String = "",
            val status2: String = "",
            val total: Int = 0,
            val user_id: Int = 0
        )
    }
}