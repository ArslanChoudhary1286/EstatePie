package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.rentfees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.enums.Plans
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.RentFeesRequest
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.MangeUtilitiesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RentFeesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.AssignPropertyToTenantsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentFeesViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo,
    private val tokenManager: TokenManager
) : ViewModel() {

    var propertyId: String = ""

    val request = RentFeesRequest()

    private val _rentFeeUiState = MutableStateFlow<NetworkResult<RentFeesResponse>>(
        NetworkResult.Idle())
    val rentFeeUiState = _rentFeeUiState.asStateFlow()

    fun getRentFees(
    ) {
        viewModelScope.launch {
            propertiesRepo.getRentFees(request).collectLatest { _rentFeeUiState.value = it }
        }
    }

    private val _propertyMultiListingUiState = MutableStateFlow<NetworkResult<MultiListingPropertyResponse>>(
        NetworkResult.Idle())
    val propertyMultiListingUiState = _propertyMultiListingUiState.asStateFlow()

    fun getMultiListingProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.getMultiListingProperties().collectLatest { _propertyMultiListingUiState.value = it }
        }
    }

    fun userPlan(): Plans{
        val userPlan: Int? = tokenManager.getCurrentUser()?.user_plan

        when(userPlan){

            1 -> return Plans.FREE
            2 -> return Plans.STANDARD
            3 -> return Plans.PREMIUM
            else -> return Plans.FREE
        }
    }

}