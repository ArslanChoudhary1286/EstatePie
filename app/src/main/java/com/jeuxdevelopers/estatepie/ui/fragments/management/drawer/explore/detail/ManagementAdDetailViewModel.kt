package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.explore.MarkToFavoriteRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.MarkToFavoriteResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementAdDetailViewModel @Inject constructor(
    private val exploreRepo: ExploreRepo,
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = ViewAllRequest()

    var leaseTerms: String = "N/A"

    val markToFavoriteRequest = MarkToFavoriteRequest()

    val propertiesDetailsRequest = PropertyDetailRequest()

    private val _leasingTermsUiState = MutableStateFlow<NetworkResult<LeaseTermResponse>>(
        NetworkResult.Idle())
    val leasingTermsUiState = _leasingTermsUiState.asStateFlow()

    private val _markToFavoriteUiState = MutableStateFlow<NetworkResult<MarkToFavoriteResponse>>(
        NetworkResult.Idle())
    val markToFavoriteUiState = _markToFavoriteUiState.asStateFlow()

    private val _propertiesDetailsUiState = MutableSharedFlow<NetworkResult<AdsDetailResponse>>()
    val propertiesDetailsUiState = _propertiesDetailsUiState.asSharedFlow()

    fun leasingTerms(
    ) {
        viewModelScope.launch {
            exploreRepo.leasingTerms().collectLatest { _leasingTermsUiState.value = it }
        }
    }

    fun markToFavorite(
    ) {
        viewModelScope.launch {
            exploreRepo.markToFavorite(markToFavoriteRequest).collectLatest { _markToFavoriteUiState.value = it }
        }
    }

    fun getPropertiesDetails(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getPropertiesDetails(propertiesDetailsRequest).collectLatest { _propertiesDetailsUiState.emit(it) }
        }
    }

}