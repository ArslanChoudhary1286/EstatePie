package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.billing.UpdatePaymentScheduleRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.UpdatePaymentScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.GetBillTypesResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPaymentViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    var id : Int = 0

    val request = UpdatePaymentScheduleRequest()

    private val _updatePaymentScheduleUiState = MutableStateFlow<NetworkResult<UpdatePaymentScheduleResponse>>(
        NetworkResult.Idle())
    val updatePaymentScheduleUiState = _updatePaymentScheduleUiState.asStateFlow()

    private val _billTypesUiState = MutableStateFlow<NetworkResult<GetBillTypesResponse>>(
        NetworkResult.Idle())
    val billTypesUiState = _billTypesUiState.asStateFlow()

    fun updatePaymentSchedule(
        id: Int
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.updatePaymentSchedule(id, request).collectLatest { _updatePaymentScheduleUiState.value = it }
        }
    }

    fun getBillTypes(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getBillTypes().collectLatest { _billTypesUiState.value = it }
        }
    }

}