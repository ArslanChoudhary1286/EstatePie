package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard.allreports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.dashboard.AllReportsRequest
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.AllReportsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.MangeUtilitiesResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllReportsViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val request = AllReportsRequest()

    var startDate: String = ""

    var endDate: String = ""

    private val _allReportsUiState = MutableStateFlow<NetworkResult<AllReportsResponse>>(
        NetworkResult.Idle())
    val allReportsUiState = _allReportsUiState.asStateFlow()

    fun getAllReports(
    ) {
        if (startDate.isNotEmpty() && endDate.isNotEmpty())
        request.date = startDate + "," + endDate
        viewModelScope.launch {

            propertiesRepo.getAllReports(request).collectLatest { _allReportsUiState.value = it }
        }
    }


}