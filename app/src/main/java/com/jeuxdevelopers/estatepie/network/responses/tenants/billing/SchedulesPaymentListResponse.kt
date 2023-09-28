package com.jeuxdevelopers.estatepie.network.responses.tenants.billing

data class SchedulesPaymentListResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val last_page: Int = 0,
        val next_page_url: Any = "",
        val schedules: List<Schedule> = listOf()
    ){
        data class Schedule(
            val amount: String = "",
            val id: Int = 0,
            val is_schedule: Boolean = false,
            val pick_date: String = "",
            val title: String = "",
            val user_id: Int = 0
        )
    }
}