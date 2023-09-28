package com.jeuxdevelopers.estatepie.network.requests.management.csv

import java.io.File

data class UploadCSVRequest(

    var id : String = "",
    var file: File? = null,
)
