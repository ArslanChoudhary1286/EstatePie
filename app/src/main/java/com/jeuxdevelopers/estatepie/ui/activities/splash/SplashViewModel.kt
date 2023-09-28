package com.jeuxdevelopers.estatepie.ui.activities.splash

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
class SplashViewModel @Inject constructor(
    // private val usersRepo: UsersRepo
) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager
    private val _splashUiState = MutableStateFlow(false)
    val splashUiState = _splashUiState.asStateFlow()


    init {
        viewModelScope.launch {
            delay(3000)
            _splashUiState.value = true
        }
    }

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }
}