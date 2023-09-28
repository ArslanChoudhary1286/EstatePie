package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteAdsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.GetAdsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.GetCommercialRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AdsPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteAdsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdsViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val adsRequest = GetAdsRequest()

    val deleteAdsRequest = DeleteAdsRequest()

    private val _adUiState = MutableStateFlow<NetworkResult<AdsPropertiesResponse>>(
        NetworkResult.Idle())
    val adUiState = _adUiState.asStateFlow()

    private val _deleteAdsPropertyUiState = MutableSharedFlow<NetworkResult<DeleteAdsResponse>>()
    val deleteAdsPropertyUiState = _deleteAdsPropertyUiState.asSharedFlow()

    fun getAdsProperties(
    ) {
        viewModelScope.launch {
            propertiesRepo.getAdsProperties(adsRequest).collectLatest { _adUiState.value = it }
        }
    }

    fun deleteAdsProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.deleteAdsProperties(deleteAdsRequest).collectLatest { _deleteAdsPropertyUiState.emit(it) }
        }
    }

}