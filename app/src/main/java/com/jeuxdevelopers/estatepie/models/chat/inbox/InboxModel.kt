package com.jeuxdevelopers.estatepie.models.chat.inbox

import com.google.firebase.firestore.DocumentId

data class InboxModel(

    @DocumentId
    var Id: String? = "",

    var created_at:  Long = 0,
    var is_active:  Boolean = false,
    var is_deleted:  Boolean = false,

    var landlord_id: Int = 0,
    var landlord_name: String = "",
    var landlord_image: String = "",

    var tenant_id: Int = 0,
    var tenant_name: String = "",
    var tenant_image: String = "",

    var property_id: Int = 0,
    var property_name: String = "",
    var property_image: String = "",

    var last_msg: String = "",

    var unread_landlord_msg_count:  Boolean = false,
    var unread_tenant_msg_count: Boolean = false,

)
