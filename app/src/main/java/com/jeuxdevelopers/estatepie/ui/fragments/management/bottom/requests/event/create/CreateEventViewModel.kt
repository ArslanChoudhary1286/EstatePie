package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.requests.event.create

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.management.properties.AddResidentialRequest
import com.jeuxdevelopers.estatepie.network.requests.management.requestReport.AddNewEventRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AddResidentialResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.MultiListingPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.AddNewEventResponse
import com.jeuxdevelopers.estatepie.network.responses.management.requestReports.AllEventTypesResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var date: String = ""

    var startTime: Long = 0

    var propertyId: String = ""

    var eventId: String = ""

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    var addNewEventRequest = AddNewEventRequest()

    private val _addNewEventUiState = MutableStateFlow<NetworkResult<AddNewEventResponse>>(
        NetworkResult.Idle())
    val addNewEventUiState = _addNewEventUiState.asStateFlow()

    fun addNewEvent() {
        viewModelScope.launch {
            propertiesRepo.addNewEvent(addNewEventRequest).collectLatest { _addNewEventUiState.value = it }
        }
    }

    private val _propertyMultiListingUiState = MutableStateFlow<NetworkResult<MultiListingPropertyResponse>>(
        NetworkResult.Idle())
    val propertyMultiListingUiState = _propertyMultiListingUiState.asStateFlow()

    fun getMultiListingProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.getMultiListingProperties().collectLatest { _propertyMultiListingUiState.value = it }
        }
    }

    private val _eventsTypesUiState = MutableStateFlow<NetworkResult<AllEventTypesResponse>>(
        NetworkResult.Idle())
    val eventsTypesUiState = _eventsTypesUiState.asStateFlow()

    fun getEventsTypes(
    ) {
        viewModelScope.launch {
            propertiesRepo.getEventsTypes().collectLatest { _eventsTypesUiState.value = it }
        }
    }

}