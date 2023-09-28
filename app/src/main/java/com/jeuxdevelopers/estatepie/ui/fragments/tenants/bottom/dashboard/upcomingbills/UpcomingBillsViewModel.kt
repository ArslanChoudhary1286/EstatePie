package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.GetBillTypesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingBillsViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = ListOfBillsRequest()

    private val _upComingBillsUiState = MutableStateFlow<NetworkResult<ListOfBillsResponse>>(
        NetworkResult.Idle())
    val upComingBillsUiState = _upComingBillsUiState.asStateFlow()

    private val _billTypesUiState = MutableStateFlow<NetworkResult<GetBillTypesResponse>>(
        NetworkResult.Idle())
    val billTypesUiState = _billTypesUiState.asStateFlow()

    fun getUpcomingBills(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getUpcomingBills(request).collectLatest { _upComingBillsUiState.value = it }
        }
    }

    fun getBillTypes(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getBillTypes().collectLatest { _billTypesUiState.value = it }
        }
    }

}