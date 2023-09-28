package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.billing.AddPaymentScheduleRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.AddPaymentScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.GetBillTypesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPayScheduleViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = AddPaymentScheduleRequest()

    private val _addPaymentScheduleUiState = MutableStateFlow<NetworkResult<AddPaymentScheduleResponse>>(
        NetworkResult.Idle())
    val addPaymentScheduleUiState = _addPaymentScheduleUiState.asStateFlow()

    private val _billTypesUiState = MutableStateFlow<NetworkResult<GetBillTypesResponse>>(
        NetworkResult.Idle())
    val billTypesUiState = _billTypesUiState.asStateFlow()

    fun addPaymentSchedule(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.addPaymentSchedule(request).collectLatest { _addPaymentScheduleUiState.value = it }
        }
    }

    fun getBillTypes(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getBillTypes().collectLatest { _billTypesUiState.value = it }
        }
    }

}