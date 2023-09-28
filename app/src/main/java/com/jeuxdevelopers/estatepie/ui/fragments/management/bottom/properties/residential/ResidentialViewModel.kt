package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.residential

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteCommercialRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteResidentialRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.GetResidentialRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteCommercialResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResidentialViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val residentialRequest = GetResidentialRequest()

    val deleteResidentialRequest = DeleteResidentialRequest()

    private val _residentialUiState = MutableStateFlow<NetworkResult<ResidentialPropertiesResponse>>(
        NetworkResult.Idle())
    val residentialUiState = _residentialUiState.asStateFlow()

    private val _deleteResidentialPropertyUiState = MutableSharedFlow<NetworkResult<DeleteResidentialResponse>>()
    val deleteResidentialPropertyUiState = _deleteResidentialPropertyUiState.asSharedFlow()

    fun getResidentialProperties(
    ) {
        viewModelScope.launch {
            propertiesRepo.getResidentialProperties(residentialRequest).collectLatest { _residentialUiState.value = it }
        }
    }

    fun deleteCommercialProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.deleteResidentialProperty(deleteResidentialRequest).collectLatest { _deleteResidentialPropertyUiState.emit(it)}
        }
    }

}