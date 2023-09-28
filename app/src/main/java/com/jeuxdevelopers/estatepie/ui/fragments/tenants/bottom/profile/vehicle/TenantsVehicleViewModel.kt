package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.profile.DeleteVehicleRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.DeleteVehicleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.profile.GetVehicleResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsVehicleViewModel @Inject constructor(
    private val repo: TenantsPropertiesRepo
) : ViewModel() {

    val deleteVehicleRequest = DeleteVehicleRequest()

    private val _vehicleUiState = MutableStateFlow<NetworkResult<GetVehicleResponse>>(
        NetworkResult.Idle())
    val vehicleUiState = _vehicleUiState.asStateFlow()

    private val _deleteVehicleUiState = MutableStateFlow<NetworkResult<DeleteVehicleResponse>>(
        NetworkResult.Idle())
    val deleteVehicleUiState = _deleteVehicleUiState.asStateFlow()

    fun getVehicleList(
    ) {
        viewModelScope.launch {
            repo.getVehicleList().collectLatest { _vehicleUiState.value = it }
        }
    }

    fun deleteVehicle(
    ) {
        viewModelScope.launch {
            repo.deleteVehicle(deleteVehicleRequest).collectLatest { _deleteVehicleUiState.value = it }
        }
    }

}