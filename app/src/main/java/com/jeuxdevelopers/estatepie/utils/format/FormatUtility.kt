package com.jeuxdevelopers.estatepie.utils.format

import android.location.Address
import com.google.android.gms.maps.model.LatLng

object AreaFormat {

    fun getArea(area: Double): String {

        return "%.2f m²".format(area)
    }
}

object AddressFormat {

    fun getCityCountry(city: String, country: String): String {
        return "$city, $country"
    }
}

object LocationFormat {

    fun getLatLng(latLng: LatLng): String {

        return "${latLng.latitude}, ${latLng.longitude}"
    }
}

object PerimeterFormat {

    fun getPerimeter(perimeter: Double): String {

        return "%.2f m".format(perimeter)
    }

}

object AreaAndPerimeterFormat {

    fun getAreaAndPerimeter(area: Double, perimeter: Double): String {

        return "${AreaFormat.getArea(area)} & ${PerimeterFormat.getPerimeter(perimeter)}"

    }

}
