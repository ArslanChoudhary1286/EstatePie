package com.jeuxdevelopers.estatepie.network.requests.management.tenants

data class AssignPropertyRequest(

    var user_id:String = "",
    var property_id:String = "",
    var lease_due_date:String = "",
    var lease_duration:String = "",
    var lease_amount:String = "",
    var move_in_date:String = "",
    var notes:String = ""

)
