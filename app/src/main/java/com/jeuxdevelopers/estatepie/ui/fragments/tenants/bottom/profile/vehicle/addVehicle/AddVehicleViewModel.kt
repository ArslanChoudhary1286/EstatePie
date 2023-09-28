package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle.addVehicle

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.profile.AddVehicleRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AddResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.AddVehicleResponse
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
class AddVehicleViewModel @Inject constructor(
    private val repo: TenantsPropertiesRepo
) : ViewModel() {

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    var addVehicleRequest = AddVehicleRequest()

    private val _addVehicleUiState = MutableStateFlow<NetworkResult<AddVehicleResponse>>(
        NetworkResult.Idle())
    val addVehicleUiState = _addVehicleUiState.asStateFlow()

    fun addVehicle() {
        viewModelScope.launch {
            repo.addVehicle(addVehicleRequest).collectLatest { _addVehicleUiState.value = it }
        }
    }

}