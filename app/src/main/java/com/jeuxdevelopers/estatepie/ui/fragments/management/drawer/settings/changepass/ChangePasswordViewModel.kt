package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.settings.changepass

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.management.settings.ChangePasswordRequest
import com.jeuxdevelopers.estatepie.network.responses.management.settings.ChangePasswordResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var changePasswordRequest = ChangePasswordRequest()

    private val _changePasswordState =
        MutableStateFlow<NetworkResult<ChangePasswordResponse>>(NetworkResult.Idle())

    val changePasswordState = _changePasswordState.asStateFlow()

    fun updatePassword(
    ) {
        viewModelScope.launch {
            propertiesRepo.changePassword(changePasswordRequest).collectLatest { _changePasswordState.value = it }
        }
    }

//    fun updatePassword(oldPassword: String, newPassword: String) {
//
//        viewModelScope.launch {
//            val user = Gson().fromJson(
//                sharedPreferences.getString(AppConsts.PREF_USER_MODEL, "{}"),
//                UserModel::class.java
//            )
//            authRepo.updatePassword(
//                userId = user.id.toInt(),
//                oldPassword = oldPassword,
//                newPassword = newPassword
//            ).collectLatest {
//                _updatePasswordState.value = it
//            }
//        }
//    }
}