package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.commercial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteAdsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteCommercialRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.GetCommercialRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.CommercialPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteAdsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteCommercialResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommercialViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val commercialRequest = GetCommercialRequest()

    val deleteCommercialRequest = DeleteCommercialRequest()

    private val _commercialUiState = MutableStateFlow<NetworkResult<CommercialPropertiesResponse>>(
        NetworkResult.Idle())
    val commercialUiState = _commercialUiState.asStateFlow()

    private val _deleteCommercialPropertyUiState = MutableSharedFlow<NetworkResult<DeleteCommercialResponse>>()
    val deleteCommercialPropertyUiState = _deleteCommercialPropertyUiState.asSharedFlow()

    fun getCommercialProperties(
    ) {
        viewModelScope.launch {
            propertiesRepo.getCommercialProperties(commercialRequest).collectLatest { _commercialUiState.value = it }
        }
    }

    fun deleteCommercialProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.deleteCommercialProperty(deleteCommercialRequest).collectLatest { _deleteCommercialPropertyUiState.emit(it)}
        }
    }


}