package com.jeuxdevelopers.estatepie.ui.dialogs.location

import android.location.Address
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng


class LocationPickerViewModel : ViewModel() {

    var latLng: LatLng? = null

    var address: Address? = null

}