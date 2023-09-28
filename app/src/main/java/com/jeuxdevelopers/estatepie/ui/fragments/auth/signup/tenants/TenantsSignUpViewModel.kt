package com.jeuxdevelopers.estatepie.ui.fragments.auth.signup.tenants

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantSignupRequest
import com.jeuxdevelopers.estatepie.network.responses.tenants.auth.TenantSignupResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.AppConsts.PREF_USER_MODEL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsSignUpViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val request = TenantSignupRequest()

    private val _signupUiState = MutableStateFlow<NetworkResult<TenantSignupResponse>>(NetworkResult.Idle())
    val singUpUiState = _signupUiState.asStateFlow()


    fun signUpUser(
    ) {
       viewModelScope.launch {
         authRepo.signupNewTenant(request).collectLatest {
             _signupUiState.value = it
             if (it is NetworkResult.Success){
                 it.result?.data?.user?.let { user ->
                     // saved in share prefs as well if the user successfully sign up and has data
                     sharedPreferences.edit().putString(PREF_USER_MODEL,Gson().toJson(user))
                 }
             }
         }
       }
    }

}