package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.addAds

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.AddAdsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.UpdateAdsRequest
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AddAdsPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.AmenititesResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.UpdateAdsPropertyResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAdsViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var dialogId: Int = 0

    var lease : Int = 1

    var property_type_id : Int = 0

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    val adsDetailRequest = AdsDetailRequest()

    val addAdsRequest = AddAdsRequest()

    var amenitiesName : String = ""

    var amenitiesId : String = ""

    var amenitiesList : MutableList<AmenititesResponse.Data> = mutableListOf()

    var propertyTypesResidential : MutableList<PropertyTypeResponse.Data> = mutableListOf()

    var propertyTypesCommercial : MutableList<PropertyTypeResponse.Data> = mutableListOf()

    private val _propertyTypesUiState = MutableStateFlow<NetworkResult<PropertyTypeResponse>>(
        NetworkResult.Idle())
    val propertyTypesUiState = _propertyTypesUiState.asStateFlow()

    private val _allAmenitiesUiState = MutableStateFlow<NetworkResult<AmenititesResponse>>(
        NetworkResult.Idle())
    val allAmenitiesUiState = _allAmenitiesUiState.asStateFlow()

    private val _addAdsUiState = MutableStateFlow<NetworkResult<AddAdsPropertyResponse>>(
        NetworkResult.Idle())
    val addAdsUiState = _addAdsUiState.asStateFlow()

    fun getAllAmenities() {
        viewModelScope.launch {
            propertiesRepo.getAllAmenities().collectLatest { _allAmenitiesUiState.value = it }
        }
    }

    fun getPropertyTypes() {
        viewModelScope.launch {
            propertiesRepo.getPropertyTypes().collectLatest { _propertyTypesUiState.value = it }
        }
    }

    fun addAdsProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.addAdsProperties(addAdsRequest).collectLatest { _addAdsUiState.value = it }
        }
    }


}