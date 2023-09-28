package com.jeuxdevelopers.estatepie.network.responses.management.billing

data class DeleteInvoiceResponse(
    val `data`: String,
    val message: String,
    val success: Boolean
)