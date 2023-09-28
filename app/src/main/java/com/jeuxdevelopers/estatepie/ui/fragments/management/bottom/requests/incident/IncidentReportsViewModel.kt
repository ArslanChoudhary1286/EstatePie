package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.incident

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.enums.Plans
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.IncidentWithFilterRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.IncidentWithFilterResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidentReportsViewModel @Inject constructor(
    private val managementRepo: ManagementPropertiesRepo,
    private val tokenManager: TokenManager
) : ViewModel() {

    val request = IncidentWithFilterRequest()

    var startTime: String = ""

    var endTime: String = ""

    private val _propertyMultiListingUiState = MutableStateFlow<NetworkResult<MultiListingPropertyResponse>>(
        NetworkResult.Idle())
    val propertyMultiListingUiState = _propertyMultiListingUiState.asStateFlow()

    private val _incidentWithFilterUiState = MutableStateFlow<NetworkResult<IncidentWithFilterResponse>>(
        NetworkResult.Idle())
    val incidentWithFilterUiState = _incidentWithFilterUiState.asStateFlow()

    fun getIncidentWithFilter(
    ) {
        request.date = startTime + "," + endTime
        viewModelScope.launch {
            managementRepo.getIncidentWithFilter(request).collectLatest { _incidentWithFilterUiState.value = it }
        }
    }

    fun getMultiListingProperty(
    ) {
        viewModelScope.launch {
            managementRepo.getMultiListingProperties().collectLatest { _propertyMultiListingUiState.value = it }
        }
    }

    fun userPlan(): Plans {
        val userPlan: Int? = tokenManager.getCurrentUser()?.user_plan

        when(userPlan){

            1 -> return Plans.FREE
            2 -> return Plans.STANDARD
            3 -> return Plans.PREMIUM
            else -> return Plans.FREE
        }
    }

}