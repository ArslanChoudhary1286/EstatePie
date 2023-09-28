package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.DeleteScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.SchedulesPaymentListResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentScheduleViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    private val _paymentScheduleUiState = MutableStateFlow<NetworkResult<SchedulesPaymentListResponse>>(
        NetworkResult.Idle())
    val paymentScheduleUiState = _paymentScheduleUiState.asStateFlow()

    private val _deletePaymentScheduleUiState = MutableSharedFlow<NetworkResult<DeleteScheduleResponse>>()
    val deletePaymentScheduleUiState = _deletePaymentScheduleUiState.asSharedFlow()

    fun getPaymentScheduleList(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getPaymentScheduleList().collectLatest { _paymentScheduleUiState.value = it }
        }
    }

    fun deletePaymentSchedule(
        id: Int
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.deletePaymentSchedule(id).collectLatest { _deletePaymentScheduleUiState.emit(it)}
        }
    }


}