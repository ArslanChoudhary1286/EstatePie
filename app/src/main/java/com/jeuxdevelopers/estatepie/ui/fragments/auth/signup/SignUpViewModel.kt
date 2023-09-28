package com.jeuxdevelopers.estatepie.ui.fragments.auth.signup

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.network.requests.management.auth.ManagementSignupRequest
import com.jeuxdevelopers.estatepie.network.responses.management.auth.ManagementSignUpResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.auth.TenantSignupResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.AppConsts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    val request = ManagementSignupRequest()

    private val _signupUiState = MutableStateFlow<NetworkResult<ManagementSignUpResponse>>(NetworkResult.Idle())
    val singUpUiState = _signupUiState.asStateFlow()


    fun signUpUser(
    ) {
        viewModelScope.launch {
            authRepo.signupNewManagement(request).collectLatest {
                _signupUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.user?.let { user ->
                        // saved in share prefs as well if the user successfully sign up and has data
                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                    }
                }
            }
        }
    }

}