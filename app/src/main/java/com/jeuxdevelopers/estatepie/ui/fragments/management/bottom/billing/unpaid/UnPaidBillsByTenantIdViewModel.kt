package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.unpaid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.billing.DeleteInvoiceRequest
import com.jeuxdevelopers.estatepie.network.requests.management.billing.UnPaidBillByTenantIdRequest
import com.jeuxdevelopers.estatepie.network.responses.management.billing.DeleteInvoiceResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillByTenantIdResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnPaidBillsByTenantIdViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val request = UnPaidBillByTenantIdRequest()

    val deleteInvoiceRequest = DeleteInvoiceRequest()

    private val _unPaidBillsByTenantIdUiState = MutableStateFlow<NetworkResult<UnPaidBillByTenantIdResponse>>(
        NetworkResult.Idle())
    val unPaidBillsByTenantIdUiState = _unPaidBillsByTenantIdUiState.asStateFlow()

    private val _deleteInvoiceUiState = MutableSharedFlow<NetworkResult<DeleteInvoiceResponse>>()
    val deleteInvoiceUiState = _deleteInvoiceUiState.asSharedFlow()

    fun getUnPaidBillsByTenantId(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getUnpaidBillByTenantId(request).collectLatest { _unPaidBillsByTenantIdUiState.value = it }
        }
    }

    fun deleteInvoice(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.deleteInvoice(deleteInvoiceRequest).collectLatest { _deleteInvoiceUiState.emit(it) }
        }
    }

}