package com.jeuxdevelopers.estatepie.ui.fragments.intro

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.enums.UserType
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.CheckSocialUserRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.SocialLoginRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantSignupRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.CheckSocialUserResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.auth.TenantSignupResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.Response
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor (

    private val authRepo: AuthRepo,
    private val tokenManager: TokenManager

        ): ViewModel() {


    var userType: UserType = UserType.Tenants

    val socialRequest = SocialLoginRequest()

    val checkSocialUserRequest = CheckSocialUserRequest()

    private val _socialLoginUiState = MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Idle())
    val socialLoginUiState = _socialLoginUiState.asStateFlow()


    private val _checkSocialLoginUiState = MutableStateFlow<NetworkResult<CheckSocialUserResponse>>(NetworkResult.Idle())
    val checkSocialLoginUiState = _checkSocialLoginUiState.asStateFlow()


    fun socialLogin(
    ) {
        viewModelScope.launch {
            authRepo.socialLogin(socialRequest).collectLatest {
                _socialLoginUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.user?.let { user ->
                        // saved in share prefs as well if the user successfully sign up and has data
                        if (user.is_verify.isEmpty()){
                            tokenManager.saveCurrentUser(user)
                            tokenManager.saveToken(user.access_token)
                        }

                    }
                }
            }
        }
    }

    fun checkSocialLogin(
    ) {
        viewModelScope.launch {
            authRepo.checkSocialUser(checkSocialUserRequest).collectLatest {
                _checkSocialLoginUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.let { user ->
                    }
                }
            }
        }
    }

}