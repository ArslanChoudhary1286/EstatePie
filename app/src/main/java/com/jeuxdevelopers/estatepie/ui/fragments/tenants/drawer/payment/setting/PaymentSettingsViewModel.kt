package com.jeuxdevelopers.estatepie.ui.fragments.tenants.drawer.payment.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.plans.CreateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.UpdateCustomerRequest
import com.jeuxdevelopers.estatepie.network.responses.plans.CardDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.plans.CreateCustomerResponse
import com.jeuxdevelopers.estatepie.network.responses.plans.UpdateCustomerResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.DeleteScheduleResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.billing.SchedulesPaymentListResponse
import com.jeuxdevelopers.estatepie.repos.Plans.PlansRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentSettingsViewModel @Inject constructor(
    private val plansRepo: PlansRepo
) : ViewModel() {

    var nounce_id: String = ""

    val createCustomerRequest = CreateCustomerRequest()

    val updateCustomerRequest = UpdateCustomerRequest()

    private val _cardDetailUiState = MutableStateFlow<NetworkResult<CardDetailResponse>>(
        NetworkResult.Idle())
    val cardDetailUiState = _cardDetailUiState.asStateFlow()

    private val _addCardUiState = MutableSharedFlow<NetworkResult<CreateCustomerResponse>>()
    val addCardUiState = _addCardUiState.asSharedFlow()

    private val _updateCardUiState = MutableSharedFlow<NetworkResult<UpdateCustomerResponse>>()
    val updateCardUiState = _updateCardUiState.asSharedFlow()

    fun getCardDetail(
    ) {
        viewModelScope.launch {
            plansRepo.getCardDetail().collectLatest { _cardDetailUiState.value = it }
        }
    }

    fun addCustomerCard(
    ) {
        viewModelScope.launch {
            plansRepo.addCustomerCard(createCustomerRequest).collectLatest { _addCardUiState.emit(it)}
        }
    }

    fun updateCustomerCard() {
        viewModelScope.launch {
            plansRepo.updateCustomerCard(nounce_id, updateCustomerRequest).collectLatest { _updateCardUiState.emit(it)}
        }
    }


}