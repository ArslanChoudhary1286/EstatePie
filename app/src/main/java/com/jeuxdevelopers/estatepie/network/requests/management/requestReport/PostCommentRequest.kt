package com.jeuxdevelopers.estatepie.network.requests.management.requestReport

data class PostCommentRequest(
    var request_id: String = "",
    var comments: String = ""
)
