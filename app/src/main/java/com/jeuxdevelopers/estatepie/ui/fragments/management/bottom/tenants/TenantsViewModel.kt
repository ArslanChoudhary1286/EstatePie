package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.tenants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.tenants.MyTenantsRequest
import com.jeuxdevelopers.estatepie.network.responses.management.tenants.MyTenantsResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsViewModel @Inject constructor(
    private val managementPropertiesRepo: ManagementPropertiesRepo
)  : ViewModel() {

    val request = MyTenantsRequest()

    private val _myTenantsUiState = MutableSharedFlow<NetworkResult<MyTenantsResponse>>()
    val myTenantsUiState = _myTenantsUiState.asSharedFlow()

    fun getMyTenantsList(
    ) {
        viewModelScope.launch {
            managementPropertiesRepo.getMyTenantsList(request).collectLatest { _myTenantsUiState.emit(it)}
        }
    }

}