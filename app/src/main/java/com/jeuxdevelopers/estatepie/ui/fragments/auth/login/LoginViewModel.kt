package com.jeuxdevelopers.estatepie.ui.fragments.auth.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.auth.ResendCodeRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.CheckSocialUserRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.SocialLoginRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantLoginRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.CheckSocialUserResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ResendCodeResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val sharedPreferences: SharedPreferences,
    private val tokenManager: TokenManager
) : ViewModel() {

    val request = TenantLoginRequest()

    private val _loginUiState = MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Idle())
    val loginUiState = _loginUiState.asStateFlow()

    val resendCodeRequest = ResendCodeRequest()

    private val _resendCodeUiState = MutableStateFlow<NetworkResult<ResendCodeResponse>>(NetworkResult.Idle())
    val resendCodeUiState = _resendCodeUiState.asStateFlow()

    val socialRequest = SocialLoginRequest()

    private val _socialLoginUiState = MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Idle())
    val socialLoginUiState = _socialLoginUiState.asStateFlow()

    val checkSocialUserRequest = CheckSocialUserRequest()

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


    fun loginUser(
    ) {
        viewModelScope.launch {
            authRepo.loginUser(request).collectLatest {
                _loginUiState.value = it
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

    fun resendCode(
    ) {
        viewModelScope.launch {
            authRepo.resendCode(resendCodeRequest).collectLatest {
                _resendCodeUiState.value = it
            }
        }
    }

}