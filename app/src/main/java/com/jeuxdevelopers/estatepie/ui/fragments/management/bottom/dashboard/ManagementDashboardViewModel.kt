package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.models.management.ManagementDashboardModel
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementDashboardViewModel @Inject constructor(
    // private val usersRepo: UsersRepo
) : ViewModel() {


    val list = ArrayList<ManagementDashboardModel>()

    @Inject
    lateinit var tokenManager: TokenManager
    private val _dashboardUiState = MutableStateFlow(false)

    val dashboardUiState = _dashboardUiState.asStateFlow()

//    init {
//        viewModelScope.launch {
//            delay(500)
//            _dashboardUiState.value = true
//        }
//    }

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }
}