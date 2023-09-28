package com.jeuxdevelopers.estatepie.network.responses.tenants.others

data class PropertyTypesResponse(
    var `data`: List<Data> = listOf(),
    var max: Int = 0,
    var message: String = "",
    var min: Int = 0,
    var success: Boolean = false
) {
    data class Data(
        var category_id: Int = 0,
        var id: Int = 0,
        var name: String = ""
    )
}