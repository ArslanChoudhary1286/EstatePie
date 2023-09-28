package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.MaintenanceWithFilterRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.MaintenanceWithFilterResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceRequestsViewModel @Inject constructor(
    private val managementRepo: ManagementPropertiesRepo
) : ViewModel() {

    val request = MaintenanceWithFilterRequest()

    var startTime: String = ""

    var endTime: String = ""

    private val _propertyMultiListingUiState = MutableStateFlow<NetworkResult<MultiListingPropertyResponse>>(
        NetworkResult.Idle())
    val propertyMultiListingUiState = _propertyMultiListingUiState.asStateFlow()

    private val _maintenanceWithFilterUiState = MutableStateFlow<NetworkResult<MaintenanceWithFilterResponse>>(
        NetworkResult.Idle())
    val maintenanceWithFilterUiState = _maintenanceWithFilterUiState.asStateFlow()

    fun getMaintenanceWithFilter(
    ) {
        request.date = startTime + "," + endTime
        viewModelScope.launch {
            managementRepo.getMaintenanceWithFilter(request).collectLatest { _maintenanceWithFilterUiState.value = it }
        }
    }

    fun getMultiListingProperty(
    ) {
        viewModelScope.launch {
            managementRepo.getMultiListingProperties().collectLatest { _propertyMultiListingUiState.value = it }
        }
    }

}