package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.EventsDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.EventsDetailResponse
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
class EventsUpdatesDetailViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    val request = EventsDetailRequest()

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

    private val _requestsDetailUiState = MutableStateFlow<NetworkResult<EventsDetailResponse>>(
        NetworkResult.Idle())
    val requestsDetailUiState = _requestsDetailUiState.asStateFlow()


    fun getEventsDetail(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getEventsDetail(request).collectLatest { _requestsDetailUiState.value = it }
        }
    }

}