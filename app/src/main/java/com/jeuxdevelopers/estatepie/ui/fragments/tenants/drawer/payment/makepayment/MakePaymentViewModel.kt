package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.makepayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.ListOfBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.plans.PayBillResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.ListOfBillsResponse
import com.jeuxdevelopers.estatepie.repos.Plans.PlansRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MakePaymentViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = ListOfBillsRequest()

    private val _upPaidBillsUiState = MutableStateFlow<NetworkResult<ListOfBillsResponse>>(
        NetworkResult.Idle())
    val upPaidBillsUiState = _upPaidBillsUiState.asStateFlow()

    fun getUnPaidBills(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getUpcomingBills(request).collectLatest { _upPaidBillsUiState.value = it }
        }
    }

}