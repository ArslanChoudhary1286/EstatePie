package com.jeuxdevelopers.estatepie.network.requests.management.tenants

data class AddNoteToTenantsRequest(
    var property_id: String  = "",
    var message: String  = "",
    var type: String  = "",
    var tenant_id: String  = ""
)
