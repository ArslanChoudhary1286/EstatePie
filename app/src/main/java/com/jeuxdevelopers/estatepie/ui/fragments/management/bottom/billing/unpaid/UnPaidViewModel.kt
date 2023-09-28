package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.unpaid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.responses.management.billing.UnPaidBillsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnPaidViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    private val _unPaidBillsUiState = MutableStateFlow<NetworkResult<UnPaidBillsResponse>>(
        NetworkResult.Idle())
    val unPaidBillsUiState = _unPaidBillsUiState.asStateFlow()

    fun getUnPaidBills(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getUnPaidBills().collectLatest { _unPaidBillsUiState.value = it }
        }
    }
}