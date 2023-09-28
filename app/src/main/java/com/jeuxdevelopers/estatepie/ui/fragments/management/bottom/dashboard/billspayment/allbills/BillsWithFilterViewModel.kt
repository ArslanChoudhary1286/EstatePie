package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment.allbills

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.PropertyBillsRequest
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.PropertyBillsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsWithFilterViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val request = PropertyBillsRequest()

    private val _propertyBillsUiState = MutableStateFlow<NetworkResult<PropertyBillsResponse>>(
        NetworkResult.Idle())
    val propertyBillsUiState = _propertyBillsUiState.asStateFlow()


    fun getPropertyBills(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getPropertyBills(request).collectLatest { _propertyBillsUiState.value = it }
        }
    }

}