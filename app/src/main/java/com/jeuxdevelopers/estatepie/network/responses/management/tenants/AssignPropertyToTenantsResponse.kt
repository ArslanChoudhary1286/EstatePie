package com.jeuxdevelopers.estatepie.network.responses.management.tenants

data class AssignPropertyToTenantsResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val created_at: String,
        val duration: String,
        val end_date: String,
        val id: Int,
        val lease_amount: String,
        val notes: String,
        val property_id: Int,
        val start_date: String,
        val updated_at: String,
        val user_id: Int
    )
}