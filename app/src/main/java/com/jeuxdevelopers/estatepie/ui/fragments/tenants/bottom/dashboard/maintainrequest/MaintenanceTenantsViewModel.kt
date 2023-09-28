package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.maintainrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.MaintenanceRequestRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.MaintenanceRequestResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceTenantsViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = MaintenanceRequestRequest()

    var startTime: String = ""

    var endTime: String = ""

    private val _maintenanceRequestsIdUiState = MutableStateFlow<NetworkResult<MaintenanceRequestResponse>>(
        NetworkResult.Idle())
    val maintenanceRequestsIdUiState = _maintenanceRequestsIdUiState.asStateFlow()

    fun getMaintenanceRequests(
    ) {
        request.date = startTime + "," + endTime
        viewModelScope.launch {
            tenantsPropertiesRepo.getMaintenanceRequests(request).collectLatest { _maintenanceRequestsIdUiState.value = it }
        }
    }
}