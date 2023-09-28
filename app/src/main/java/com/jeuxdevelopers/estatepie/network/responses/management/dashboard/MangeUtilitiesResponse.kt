package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class MangeUtilitiesResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val utilities: List<Utility> = listOf()
    ){
        data class Utility(
            val address: String = "",
            val image: String = "",
            val name: String = "",
            val property_id: Int = 0,
            val total: String = "",
            val user_id: Int = 0
        )
    }
}