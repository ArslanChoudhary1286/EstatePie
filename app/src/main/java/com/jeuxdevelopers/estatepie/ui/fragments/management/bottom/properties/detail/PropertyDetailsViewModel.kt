package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.AddNoteToTenantsRequest
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.DelNoteToTenantsRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.MarkToFavoriteResponse
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertiesDetailsResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.AddNoteToTenantResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.DelNotesByIdResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val exploreRepo: ExploreRepo,
    private val propertiesRepo: ManagementPropertiesRepo
    ) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    var id : String = ""

    var shareUrl : String = ""

    var tenantId : String = ""

    val propertiesDetailsRequest = PropertyDetailRequest()

    private val _leasingTermsUiState = MutableStateFlow<NetworkResult<LeaseTermResponse>>(
        NetworkResult.Idle())
    val leasingTermsUiState = _leasingTermsUiState.asStateFlow()

    private val _propertiesDetailsUiState = MutableStateFlow<NetworkResult<PropertiesDetailsResponse>>(
        NetworkResult.Idle())
    val propertiesDetailsUiState = _propertiesDetailsUiState.asStateFlow()


    val addNoteToTenantsRequest = AddNoteToTenantsRequest()

    private val _deleteNotesByIdUiState = MutableStateFlow<NetworkResult<DelNotesByIdResponse>>(
        NetworkResult.Idle())
    val deleteNotesByIdUiState = _deleteNotesByIdUiState.asStateFlow()

    val delNotesByIdRequest = DelNoteToTenantsRequest()

    private val _addNoteToTenantsUiState = MutableStateFlow<NetworkResult<AddNoteToTenantResponse>>(
        NetworkResult.Idle())
    val addNoteToTenantsUiState = _addNoteToTenantsUiState.asStateFlow()

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }


    fun leasingTerms(
    ) {
        viewModelScope.launch {
            exploreRepo.leasingTerms().collectLatest { _leasingTermsUiState.value = it }
        }
    }
    fun getPropertiesDetails(
    ) {
        viewModelScope.launch {
            propertiesRepo.getPropertiesDetails(propertiesDetailsRequest).collectLatest { _propertiesDetailsUiState.value = it }
        }
    }

    fun addNotes(
    ) {
        viewModelScope.launch {
            propertiesRepo.addNotesToTenants(addNoteToTenantsRequest).collectLatest { _addNoteToTenantsUiState.value = it }
        }
    }

    fun deleteNotes(
    ) {
        viewModelScope.launch {
            propertiesRepo.deleteNote(delNotesByIdRequest).collectLatest { _deleteNotesByIdUiState.value = it }
        }
    }

}