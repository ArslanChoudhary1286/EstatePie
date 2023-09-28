package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.upcomingbills.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.plans.PayBillRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.plans.PayBillResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ViewBillDetailsResponse
import com.jeuxdevelopers.estatepie.repos.Plans.PlansRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnPaidBillDetailViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo,
    private val plansRepo: PlansRepo
) : ViewModel() {

    var id : String = ""

    var status: String = ""

    val request = PayBillRequest()

    private val _billDetailUiState = MutableStateFlow<NetworkResult<ViewBillDetailsResponse>>(
        NetworkResult.Idle())
    val billDetailUiState = _billDetailUiState.asStateFlow()

    fun viewBillDetail(
        id: Int
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.viewBillDetail(id).collectLatest { _billDetailUiState.value = it }
        }
    }

    private val _payBillUiState = MutableSharedFlow<NetworkResult<PayBillResponse>>()
    val payBillUiState = _payBillUiState.asSharedFlow()

    fun payBill(
    ) {
        viewModelScope.launch {
            plansRepo.payBill(request).collectLatest { _payBillUiState.emit(it) }
        }
    }

}