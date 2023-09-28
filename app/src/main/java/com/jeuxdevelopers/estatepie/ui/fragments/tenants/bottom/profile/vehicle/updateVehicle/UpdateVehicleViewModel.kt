package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle.updateVehicle

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.management.properties.UpdateResidentialRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.profile.UpdateVehicleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.UpdateResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.UpdateVehicleResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateVehicleViewModel @Inject constructor(
    private val repo: TenantsPropertiesRepo
) : ViewModel() {

    var id : Int = 0

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    val updateVehicleRequest = UpdateVehicleRequest()

    private val _updateVehicleUiState = MutableStateFlow<NetworkResult<UpdateVehicleResponse>>(
        NetworkResult.Idle())
    val updateVehicleUiState = _updateVehicleUiState.asStateFlow()


    fun updateVehicle(
    ) {
        viewModelScope.launch {
            repo.updateVehicle(updateVehicleRequest).collectLatest { _updateVehicleUiState.value = it }
        }
    }


}