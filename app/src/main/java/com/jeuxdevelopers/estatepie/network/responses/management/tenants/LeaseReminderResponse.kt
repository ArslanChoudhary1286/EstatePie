package com.jeuxdevelopers.estatepie.network.responses.management.tenants

data class LeaseReminderResponse(
    val `data`: Boolean,
    val message: String,
    val success: Boolean
)