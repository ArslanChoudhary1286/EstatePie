package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.billing.sendinvoice

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.billing.SendInvoiceRequest
import com.jeuxdevelopers.estatepie.network.responses.management.billing.PropertiesWithBillTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.management.billing.SendInvoiceResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendInvoiceViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var imageUri: Uri? = null

    var imageBitmap: Bitmap? = null

    var billTypeId: String = ""

    var propertyId: String = ""

    var units: String = ""

    var sendInvoiceRequest = SendInvoiceRequest()

    private val _propertiesWithBillTypesUiState = MutableStateFlow<NetworkResult<PropertiesWithBillTypeResponse>>(
        NetworkResult.Idle())
    val propertiesWithBillTypesUiState = _propertiesWithBillTypesUiState.asStateFlow()

    private val _sendInvoiceUiState = MutableStateFlow<NetworkResult<SendInvoiceResponse>>(
        NetworkResult.Idle())
    val sendInvoiceUiState = _sendInvoiceUiState.asStateFlow()


    fun getPropertiesWithBillTypes(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getPropertiesWithBillTypes().collectLatest { _propertiesWithBillTypesUiState.value = it }
        }
    }

    fun sendInvoice() {
        viewModelScope.launch {
            managementPropertiesRepo.sendInvoice(sendInvoiceRequest).collectLatest { _sendInvoiceUiState.value = it }
        }
    }

}