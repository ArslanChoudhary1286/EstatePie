package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.addResidential

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.management.properties.AddResidentialRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AddResidentialResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddResidentialViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    var addResidentialRequest = AddResidentialRequest()

    private val _addResidentialPropertyUiState = MutableStateFlow<NetworkResult<AddResidentialResponse>>(
        NetworkResult.Idle())
    val addResidentialPropertyUiState = _addResidentialPropertyUiState.asStateFlow()

    fun addResidentialProperty() {
        viewModelScope.launch {
            propertiesRepo.addResidentialProperty(addResidentialRequest).collectLatest { _addResidentialPropertyUiState.value = it }
        }
    }

}