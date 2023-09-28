package com.jeuxdevelopers.estatepie.ui.fragments.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.notification.ReadNotificationRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteResidentialRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.GetResidentialRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.DeleteResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.ResidentialPropertiesResponse
import com.jeuxdevelopers.estatepie.network.responses.notification.GetNotificationsResponse
import com.jeuxdevelopers.estatepie.network.responses.notification.ReadNotificationResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    val readNotificationRequest = ReadNotificationRequest()

    private val _notificationUiState = MutableStateFlow<NetworkResult<GetNotificationsResponse>>(
        NetworkResult.Idle())
    val notificationUiState = _notificationUiState.asStateFlow()

    private val _readNotificationUiState = MutableSharedFlow<NetworkResult<ReadNotificationResponse>>()
    val readNotificationUiState = _readNotificationUiState.asSharedFlow()

    fun getAllNotifications(
    ) {
        viewModelScope.launch {
            propertiesRepo.getAllNotifications().collectLatest { _notificationUiState.value = it }
        }
    }

    fun readNotification(
    ) {
        viewModelScope.launch {
            propertiesRepo.readNotification(readNotificationRequest).collectLatest { _readNotificationUiState.emit(it)}
        }
    }

}