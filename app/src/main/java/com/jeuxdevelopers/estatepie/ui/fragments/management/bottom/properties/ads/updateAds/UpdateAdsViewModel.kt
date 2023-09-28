package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.ads.updateAds

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.DeleteAdsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.properties.UpdateAdsRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.*
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAdsViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var dialogId: Int = 0

    var id : Int = 0

    var lease : Int = 1

    var property_type_id : Int = 0

    var imageUri: Uri? = null

    var imageBitmapList = ArrayList<Bitmap>()

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    val adsDetailRequest = AdsDetailRequest()

    val updateAdsRequest = UpdateAdsRequest()

    var amenitiesName : String = ""

    var amenitiesId : String = ""


    var amenitiesList : MutableList<AmenititesResponse.Data> = mutableListOf()

    var propertyTypesResidential : MutableList<PropertyTypeResponse.Data> = mutableListOf()

    var propertyTypesCommercial : MutableList<PropertyTypeResponse.Data> = mutableListOf()

    private val _propertiesDetailsUiState = MutableStateFlow<NetworkResult<AdsDetailResponse>>(
        NetworkResult.Idle())
    val propertiesDetailsUiState = _propertiesDetailsUiState.asStateFlow()

    private val _updateAdsUiState = MutableStateFlow<NetworkResult<UpdateAdsPropertyResponse>>(
        NetworkResult.Idle())
    val updateAdsUiState = _updateAdsUiState.asStateFlow()

    private val _propertyTypesUiState = MutableStateFlow<NetworkResult<PropertyTypeResponse>>(
        NetworkResult.Idle())
    val propertyTypesUiState = _propertyTypesUiState.asStateFlow()

    private val _allAmenitiesUiState = MutableStateFlow<NetworkResult<AmenititesResponse>>(
        NetworkResult.Idle())
    val allAmenitiesUiState = _allAmenitiesUiState.asStateFlow()

    fun getPropertiesDetails(
    ) {
        viewModelScope.launch {
            propertiesRepo.getAdsDetail(adsDetailRequest).collectLatest { _propertiesDetailsUiState.value = it }
        }
    }

    fun updateAdsProperty(
    ) {
        viewModelScope.launch {
            propertiesRepo.updateAdsProperties(updateAdsRequest).collectLatest { _updateAdsUiState.value = it }
        }
    }

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

}