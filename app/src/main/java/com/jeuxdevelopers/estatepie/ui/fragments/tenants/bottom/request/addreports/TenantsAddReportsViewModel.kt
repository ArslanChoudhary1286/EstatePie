package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.request.addreports

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.tenant.request.SendReportRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.AllAssignPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.SendReportResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsAddReportsViewModel @Inject constructor(
    private val repo: TenantsPropertiesRepo
) : ViewModel() {

    var requestType: Int = 0

    var dialogId: Int = 1

    var propertyType: Int = 0

    var imageUri: Uri? = null

    var assignPropertyList : MutableList<AllAssignPropertiesResponse.Data> = mutableListOf()

    var imageBitmapList = ArrayList<Bitmap>()

    var sendReportRequest = SendReportRequest()

    private val _assignPropertiesUiState = MutableStateFlow<NetworkResult<AllAssignPropertiesResponse>>(
        NetworkResult.Idle())
    val assignPropertiesUiState = _assignPropertiesUiState.asStateFlow()

    private val _sendReportUiState = MutableStateFlow<NetworkResult<SendReportResponse>>(
        NetworkResult.Idle())
    val sendReportUiState = _sendReportUiState.asStateFlow()

    fun sendReport() {
        viewModelScope.launch {
            repo.addRequestReport(sendReportRequest).collectLatest { _sendReportUiState.value = it }
        }
    }

    fun getAssignProperties() {
        viewModelScope.launch {
            repo.getAssignProperties().collectLatest { _assignPropertiesUiState.value = it }
        }
    }
//    fun sendRequest(title: String, description: String) {
//        viewModelScope.launch {
//            repo.addRequestReport(
//                tokenManager.getToken().toString(), title, requestType, propertyType, description,
//                File(filePath)
//            ).collectLatest {
//
//            }
//        }
//    }

}