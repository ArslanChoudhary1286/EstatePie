package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.incident

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.IncidentReportRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.IncidentReportResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenanstsIncidentReportViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = IncidentReportRequest()

    var reportList = ArrayList<IncidentReportResponse.Data.IncidentRequest>()

    private val _incidentReportsIdUiState = MutableSharedFlow<NetworkResult<IncidentReportResponse>>()
    val incidentReportsIdUiState = _incidentReportsIdUiState.asSharedFlow()

    fun getIncidentReports(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getIncidentReports(request).collectLatest { _incidentReportsIdUiState.emit(it)}
        }
    }
}