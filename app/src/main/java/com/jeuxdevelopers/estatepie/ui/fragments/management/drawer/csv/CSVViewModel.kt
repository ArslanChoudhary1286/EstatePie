package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.csv

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.csv.UploadCSVRequest
import com.jeuxdevelopers.estatepie.network.responses.management.csv.GetSampleCSVResponse
import com.jeuxdevelopers.estatepie.network.responses.management.dashboard.UploadCSVResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.report.AllAssignPropertiesResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CSVViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var downloadUrl: String = ""

    var fileName : String = ""

    var id: String = ""

    var sampleCsvList : MutableList<GetSampleCSVResponse.Data> = mutableListOf()

    val uploadCSVRequest = UploadCSVRequest()

    private val _sampleUiState =
        MutableStateFlow<NetworkResult<GetSampleCSVResponse>>(NetworkResult.Idle())
    val sampleUiState = _sampleUiState.asStateFlow()

    private val _uploadCSVUiState =
        MutableStateFlow<NetworkResult<UploadCSVResponse>>(NetworkResult.Idle())

    val uploadCSVUiState = _uploadCSVUiState.asStateFlow()

    fun getCsvFiles(
    ) {
        viewModelScope.launch {
            propertiesRepo.getCsvFiles().collectLatest { _sampleUiState.value = it }
        }
    }

    fun uploadCsvFile(
    ) {
        viewModelScope.launch {
            propertiesRepo.uploadCsvFile(uploadCSVRequest).collectLatest { _uploadCSVUiState.value = it }
        }
    }

}