package com.jeuxdevelopers.estatepie.ui.dialogs.property

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.responses.management.properties.PropertyTypeResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.others.PropertyTypesResponse
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
class PropertyTypeViewModel @Inject constructor(
    private val repo: TenantsPropertiesRepo,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _propertyTypesState =
        MutableStateFlow<NetworkResult<PropertyTypeResponse>>(NetworkResult.Idle())
    val propertyTypes = _propertyTypesState.asStateFlow()


    init {
        viewModelScope.launch {
            repo.getPropertyTypes().collectLatest {
                _propertyTypesState.value = it
            }
        }
    }
}