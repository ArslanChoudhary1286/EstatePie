package com.jeuxdevelopers.estatepie.models.chat.message

data class MessageModel(

    var chat_uid: String = "",
    var created_at: Long = 0,
    var deleted_at: Boolean = false,
    var message: String = "",
    var sender_id: Int = 0,
    var sender_name: String = "",
    var sender_image: String = "",
    var type: String = "",
//    var is_read: Boolean = false,

)
