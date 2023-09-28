package com.jeuxdevelopers.estatepie.network.requests.tenant.auth

import okhttp3.MultipartBody

data class TenantUpdateRequest(
    var first_name: String = "",
    var last_name: String = "",
    var email: String = "",
    var phone: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)