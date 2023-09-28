package com.jeuxdevelopers.estatepie.ui.activities.tenants

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.jeuxdevelopers.estatepie.network.requests.auth.DeleteAccountRequest
import com.jeuxdevelopers.estatepie.network.requests.auth.ForgotPasswordRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.DeleteAccountResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ForgotPasswordResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainTenantsViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

    val request = DeleteAccountRequest()

    private val _deleteAccountUiState = MutableStateFlow<NetworkResult<DeleteAccountResponse>>(NetworkResult.Idle())
    val deleteAccountUiState = _deleteAccountUiState.asStateFlow()

    fun deleteUserAccount(
    ) {
        viewModelScope.launch {
            authRepo.deleteUserAccount(request).collectLatest {
                _deleteAccountUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.let { user ->

                        tokenManager.saveCurrentUser(null)
                        tokenManager.saveToken("")
                        // saved in share prefs as well if the user successfully sign up and has data
//                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                    }
                }
            }
        }
    }

}