package com.jeuxdevelopers.estatepie.ui.fragments.explore.detail

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdDetailViewModel @Inject constructor(
    private val exploreRepo: ExploreRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var leasingTerms : String = ""

    var shareUrl : String = ""

    val request = ViewAllRequest()

    val adsDetailRequest = AdsDetailRequest()

    private val _leasingTermsUiState = MutableStateFlow<NetworkResult<LeaseTermResponse>>(NetworkResult.Idle())
    val leasingTermsUiState = _leasingTermsUiState.asStateFlow()

    private val _adsDetailUiState = MutableStateFlow<NetworkResult<AdsDetailResponse>>(NetworkResult.Idle())
    val adsDetailUiState = _adsDetailUiState.asStateFlow()

    fun leasingTerms(
    ) {
        viewModelScope.launch {
            exploreRepo.leasingTerms().collectLatest { _leasingTermsUiState.value = it }
        }
    }

    fun getAdsDetail(
    ) {
        viewModelScope.launch {
//            exploreRepo.getAdsDetail(adsDetailRequest).collectLatest { _adsDetailUiState.value = it }
        }
    }

}