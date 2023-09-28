package com.jeuxdevelopers.estatepie.network.responses.management.billing

data class PropertiesWithBillTypeResponse(
    val `data`: Data = Data(),
    val message: String = "",
    val success: Boolean = false
){
    data class Data(
        val bill_types: List<BillType> = listOf(),
        val properties: List<Property> = listOf()
    ){
        data class BillType(
            val id: Int = 0,
            val name: String = ""
        )

        data class Property(
            val id: Int = 0,
            val name: String = ""
        )
    }
}