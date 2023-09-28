package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.*
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.*
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsProfileViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
)  : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    var userId: String = ""

    var propertyId: String = ""

    val getTenantsByIdRequest = GetTenantsByIdRequest()

    private val _tenantsByIdUiState = MutableStateFlow<NetworkResult<TenantsByIdResponse>>(
        NetworkResult.Idle())
    val tenantsByIdUiState = _tenantsByIdUiState.asStateFlow()

    val unassignPropertyRequest = UnassignPropertyRequest()

    private val _unAssigntenantsUiState = MutableSharedFlow<NetworkResult<UnassignPropertyResponse>>()
    val unAssigntenantsUiState = _unAssigntenantsUiState.asSharedFlow()

    val leaseReminderRequest = LeaseReminderRequest()

    private val _leaseReminderUiState = MutableSharedFlow<NetworkResult<LeaseReminderResponse>>()
    val leaseReminderUiState = _leaseReminderUiState.asSharedFlow()

    val delNotesByIdRequest = DelNoteToTenantsRequest()

    private val _deleteNotesByIdUiState = MutableSharedFlow<NetworkResult<DelNotesByIdResponse>>()
    val deleteNotesByIdUiState = _deleteNotesByIdUiState.asSharedFlow()

    val addNoteToTenantsRequest = AddNoteToTenantsRequest()

    private val _addNoteToTenantsUiState = MutableSharedFlow<NetworkResult<AddNoteToTenantResponse>>()
    val addNoteToTenantsUiState = _addNoteToTenantsUiState.asSharedFlow()


    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

    fun getTenantsById(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getTenantsById(getTenantsByIdRequest).collectLatest { _tenantsByIdUiState.value = it }
        }
    }

    fun unAssignTenantProperty(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.unAssignProperty(unassignPropertyRequest).collectLatest { _unAssigntenantsUiState.emit(it)}
        }
    }

    fun leaseReminder(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.leaseReminder(leaseReminderRequest).collectLatest { _leaseReminderUiState.emit(it)}
        }
    }

    fun addNotes(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.addNotesToTenants(addNoteToTenantsRequest).collectLatest { _addNoteToTenantsUiState.emit(it)}
        }
    }

    fun deleteNotes(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.deleteNote(delNotesByIdRequest).collectLatest { _deleteNotesByIdUiState.emit(it)}
        }
    }

}