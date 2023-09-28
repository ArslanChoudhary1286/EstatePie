package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard.incidentreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.IncidentReportRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.dashboard.MaintenanceRequestRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.IncidentReportResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.dashboard.MaintenanceRequestResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidentTenantsViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    val request = IncidentReportRequest()

    var startTime: String = ""

    var endTime: String = ""

    var reportList  = ArrayList<IncidentReportResponse.Data.IncidentRequest>()

    private val _incidentReportsUiState = MutableSharedFlow<NetworkResult<IncidentReportResponse>>()
    val incidentReportsUiState = _incidentReportsUiState.asSharedFlow()

    fun getIncidentReports(
    ) {
        if (startTime.isNotEmpty() && endTime.isNotEmpty())
        request.date = startTime + "," + endTime
        viewModelScope.launch {
            tenantsPropertiesRepo.getIncidentReports(request).collectLatest { _incidentReportsUiState.emit(it)}
        }
    }
}