package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.maintenance.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.ChangeRequestStatusRequest
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.MaintenanceWithFilterDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.PostCommentRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.RequestDetailByIdResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.ChangeRequestStatusResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.MaintenanceWithFilterDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.PostCommentResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceRequestDetailViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

    val request = MaintenanceWithFilterDetailRequest()

    val changeRequestStatusRequest = ChangeRequestStatusRequest()

    val postCommentRequest = PostCommentRequest()

    private val _requestsDetailUiState = MutableStateFlow<NetworkResult<MaintenanceWithFilterDetailResponse>>(
        NetworkResult.Idle())
    val requestsDetailUiState = _requestsDetailUiState.asStateFlow()

    private val _changeStatusUiState = MutableSharedFlow<NetworkResult<ChangeRequestStatusResponse>>()
    val changeStatusUiState = _changeStatusUiState.asSharedFlow()

    private val _postCommentUiState = MutableSharedFlow<NetworkResult<PostCommentResponse>>()
    val postCommentUiState = _postCommentUiState.asSharedFlow()


    fun getMaintenanceWithFilterDetail(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getMaintenanceWithFilterDetail(request).collectLatest { _requestsDetailUiState.value = it }
        }
    }

    fun changeRequestStatus(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.changeRequestStatus(changeRequestStatusRequest).collectLatest { _changeStatusUiState.emit(it) }
        }
    }

    fun postComment(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.postComment(postCommentRequest).collectLatest { _postCommentUiState.emit(it) }
        }
    }

}