package com.jeuxdevelopers.estatepie.network.responses.others

data class TermsConditionResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val content: String = "",
        val created_at: String = "",
        val deleted_at: String = "",
        val id: Int = 0,
        val slug: String = "",
        val status: Int = 0,
        val title: String = "",
        val translations: List<Translation> = listOf(),
        val updated_at: String = ""
    ){
        data class Translation(
            val content: String = "",
            val created_at: String = "",
            val deleted_at: String = "",
            val id: Int = 0,
            val locale: String = "",
            val page_id: Int = 0,
            val status: Boolean = false,
            val title: String = "",
            val updated_at: String = ""
        )
    }
}