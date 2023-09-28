package com.jeuxdevelopers.estatepie.models.auth

import com.jeuxdevelopers.estatepie.enums.DeviceType
import com.jeuxdevelopers.estatepie.utils.AppConsts

data class UserModel(

    var id: String = AppConsts.DEVICE_TOKEN,
    var name: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNo: String = "",
    var email: String = "",
    var password: String = "",
    var deviceType: DeviceType = DeviceType.android,
    var details: Details = Details(),
    var role_id: Int = 0,
    var token_type: String = "",
    var trial_ends_at: String = "",
    var user_plan: Int = 0,
    var is_verify: String = "",
)


data class Details(
    var address: String = "",
    var business_name: String = "",
    var email_updates: Int = 0,
    var first_name: String = "",
    var id: Int = 0,
    var image: String = "",
    var image_url: String = "",
    var is_social_login: Int = 0,
    var is_verified: Int = 0,
    var last_name: String = "",
    var notifications: Int = 0,
    var phone: String = ""
)
