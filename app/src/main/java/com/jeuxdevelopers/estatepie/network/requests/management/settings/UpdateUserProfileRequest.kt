package com.jeuxdevelopers.estatepie.network.requests.management.settings

import java.io.File

data class UpdateUserProfileRequest(

    var first_name: String  = "",
    var last_name: String  = "",
    var business_name: String  = "",
    var email: String  = "",
    var phone: String  = "",
    var address: String  = "",
    var latitude: String  = "",
    var longitude: String  = "",
    var image: File? = null

    )
