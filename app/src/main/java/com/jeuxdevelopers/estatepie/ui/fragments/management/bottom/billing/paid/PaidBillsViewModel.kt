package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.paid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PaidBillsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaidBillsViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    private val _paidBillsUiState = MutableStateFlow<NetworkResult<PaidBillsResponse>>(
        NetworkResult.Idle())
    val paidBillsUiState = _paidBillsUiState.asStateFlow()

    fun getPaidBills(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getPaidBills().collectLatest { _paidBillsUiState.value = it }
        }
    }
}