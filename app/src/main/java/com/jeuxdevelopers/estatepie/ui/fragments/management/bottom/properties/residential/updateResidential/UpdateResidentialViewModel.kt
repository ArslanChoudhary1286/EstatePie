package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential.updateResidential

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.management.properties.UpdateResidentialRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.UpdateResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.AddNoteToTenantResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateResidentialViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var id : Int = 0

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    val propertiesDetailsRequest = PropertyDetailRequest()

    val updateResidentialRequest = UpdateResidentialRequest()

    private val _propertiesDetailsUiState = MutableStateFlow<NetworkResult<PropertiesDetailsResponse>>(
        NetworkResult.Idle())
    val propertiesDetailsUiState = _propertiesDetailsUiState.asStateFlow()

    private val _updateResidentialUiState = MutableStateFlow<NetworkResult<UpdateResidentialResponse>>(
        NetworkResult.Idle())
    val updateResidentialUiState = _updateResidentialUiState.asStateFlow()

    fun getPropertiesDetails(
    ) {
        viewModelScope.launch {
            propertiesRepo.getPropertiesDetails(propertiesDetailsRequest).collectLatest { _propertiesDetailsUiState.value = it }
        }
    }

    fun updateResidentialProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.updateResidentialProperty(updateResidentialRequest).collectLatest { _updateResidentialUiState.value = it }
        }
    }


}