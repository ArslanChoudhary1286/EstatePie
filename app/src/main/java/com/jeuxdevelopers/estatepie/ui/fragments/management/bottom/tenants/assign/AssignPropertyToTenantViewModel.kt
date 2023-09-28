package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.assign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.AssignPropertyRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.AssignPropertyToTenantsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignPropertyToTenantViewModel  @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var propertyId: String = ""

    var userId: String = ""

    var assignPropertyRequest = AssignPropertyRequest()

    private val _propertyMultiListingUiState = MutableStateFlow<NetworkResult<MultiListingPropertyResponse>>(
        NetworkResult.Idle())
    val propertyMultiListingUiState = _propertyMultiListingUiState.asStateFlow()

    private val _assignPropertyUiState = MutableStateFlow<NetworkResult<AssignPropertyToTenantsResponse>>(
        NetworkResult.Idle())
    val assignPropertyUiState = _assignPropertyUiState.asStateFlow()

    fun getMultiListingProperty(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getMultiListingProperties().collectLatest { _propertyMultiListingUiState.value = it }
        }
    }

    fun assignPropertyToTenant() {
        viewModelScope.launch {
            managementPropertiesRepo.assignProperty(assignPropertyRequest).collectLatest { _assignPropertyUiState.value = it }
        }
    }
}