package com.jeuxdevelopers.estatepie.network.responses.management.dashboard

data class RentFeesResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val billing_amount: List<Int> = listOf(),
        val billing_title: List<String> = listOf(),
        val chart: List<Chart> = listOf(),
        val request_data: RequestData = RequestData(),
        val total_collected: String = "",
        val vacant_property: Int = 0
    ){
        data class Chart(
            val key: String = "",
            val value: String = ""
        )

        data class RequestData(
            val incident: Incident = Incident(),
            val maintenance: Maintenance = Maintenance()
        ){
            data class Incident(
                val resolved: Int = 0,
                val under_review: Int = 0
            )

            data class Maintenance(
                val complete: Int = 0,
                val inprogress: Int = 0
            )
        }
    }
}