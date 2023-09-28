package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports.requestsByProperty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.PropertyBillsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.RequestsByPropertyIdRequest
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.PropertyBillsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RequestsByPropertyIdResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsByPropertyIdViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val request = RequestsByPropertyIdRequest()

    private val _requestsByPropertyIdUiState = MutableStateFlow<NetworkResult<RequestsByPropertyIdResponse>>(
        NetworkResult.Idle())
    val requestsByPropertyIdUiState = _requestsByPropertyIdUiState.asStateFlow()


    fun getRequestByPropertyId(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getRequestByPropertyId(request).collectLatest { _requestsByPropertyIdUiState.value = it }
        }
    }

}