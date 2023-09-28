package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.paid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.billing.DeleteInvoiceRequest
import com.jeuxdevelopers.estatepie.network.requests.management.billing.PaidBillByTenantIdRequest
import com.jeuxdevelopers.estatepie.network.responses.management.billing.DeleteInvoiceResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaidBillsByTenantIdViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val request = PaidBillByTenantIdRequest()

    val deleteInvoiceRequest = DeleteInvoiceRequest()

    private val _paidBillsByTenantIdUiState = MutableStateFlow<NetworkResult<PaidBillByTenantIdResponse>>(
        NetworkResult.Idle())
    val paidBillsByTenantIdUiState = _paidBillsByTenantIdUiState.asStateFlow()

    private val _deleteInvoiceUiState = MutableSharedFlow<NetworkResult<DeleteInvoiceResponse>>()
    val deleteInvoiceUiState = _deleteInvoiceUiState.asSharedFlow()

    fun getPaidBillsByTenantId(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getPaidBillByTenantId(request).collectLatest { _paidBillsByTenantIdUiState.value = it }
        }
    }

    fun deleteInvoice(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.deleteInvoice(deleteInvoiceRequest).collectLatest { _deleteInvoiceUiState.emit(it) }
        }
    }

}