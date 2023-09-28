package com.jeuxdevelopers.estatepie.ui.fragments.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.responses.others.TermsConditionResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    private val tenantsPropertiesRepo: TenantsPropertiesRepo
) : ViewModel() {

    private val _helpsUiState = MutableStateFlow<NetworkResult<TermsConditionResponse>>(
        NetworkResult.Idle())
    val helpsUiState = _helpsUiState.asStateFlow()

    fun getTerms(id : Int
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getTerms(id).collectLatest { _helpsUiState.value = it }
        }
    }

}