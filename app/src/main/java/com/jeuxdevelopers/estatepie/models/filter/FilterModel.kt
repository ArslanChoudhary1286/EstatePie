package com.jeuxdevelopers.estatepie.models.filter

data class FilterModel(

    var keyword: String = "",
    var location: String = "",
    var sortByPrice: String = "",
    var category: String = "",
    var propertyType: String = "",
    var bathRooms: String = "",
    var bedRooms: String = "",
    var forSaleRent: String = "",
    var fees: String = "",
    var smoking: String = "",
    var pets: String = "",
    var unit: String = "",
    var size: String = "",
    var priceStartRange: Int  = 0,
    var priceEndRange: Int = 0

)
