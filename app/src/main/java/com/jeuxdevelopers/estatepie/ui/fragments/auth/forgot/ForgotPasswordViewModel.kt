package com.jeuxdevelopers.estatepie.ui.fragments.auth.forgot

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.auth.ForgotPasswordRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.ForgotPasswordResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    val request = ForgotPasswordRequest()

    private val _forgotPasswordUiState = MutableStateFlow<NetworkResult<ForgotPasswordResponse>>(NetworkResult.Idle())
    val forgotPasswordUiState = _forgotPasswordUiState.asStateFlow()

    fun forgotPassword(
    ) {
        viewModelScope.launch {
            authRepo.forgotPassword(request).collectLatest {
                _forgotPasswordUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.let { user ->
                        // saved in share prefs as well if the user successfully sign up and has data
//                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                    }
                }
            }
        }
    }

}