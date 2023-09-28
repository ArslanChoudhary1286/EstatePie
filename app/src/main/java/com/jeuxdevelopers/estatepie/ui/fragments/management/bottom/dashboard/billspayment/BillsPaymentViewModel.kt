package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.billspayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteResidentialRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.GetResidentialRequest
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.MangeUtilitiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillsPaymentViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {


    private val _utilityBillsUiState = MutableStateFlow<NetworkResult<MangeUtilitiesResponse>>(
        NetworkResult.Idle())
    val utilityBillsUiState = _utilityBillsUiState.asStateFlow()

    fun getUtilityBills(
    ) {
        viewModelScope.launch {
            propertiesRepo.getUtilityBills().collectLatest { _utilityBillsUiState.value = it }
        }
    }


}