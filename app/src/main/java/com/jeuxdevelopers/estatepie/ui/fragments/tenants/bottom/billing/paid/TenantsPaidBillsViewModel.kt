package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.billing.paid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsPaidBillsViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = ListOfBillsRequest()

    val listOfBillsResponse = ArrayList<ListOfBillsResponse.Data.MaintenanceRequest>()

    private val _upComingBillsUiState = MutableSharedFlow<NetworkResult<ListOfBillsResponse>>()
    val upComingBillsUiState = _upComingBillsUiState.asSharedFlow()

    fun getUpcomingBills(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getUpcomingBills(request).collectLatest { _upComingBillsUiState.emit(it)}
        }
    }
}