package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsDashboardViewModel @Inject constructor(
    // private val usersRepo: UsersRepo
) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager
    private val _dashboardUiState = MutableStateFlow(false)

    val dashboardUiState = _dashboardUiState.asStateFlow()

//    init {
//        viewModelScope.launch {
//            delay(0)
//            _dashboardUiState.value = true
//        }
//    }

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

}