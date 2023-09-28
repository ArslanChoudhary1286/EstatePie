package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.notifications

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.network.responses.tenants.toggle.ToggleResponse
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.AppConsts
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val repo: TenantsPropertiesRepo,
    private val tokenManager: TokenManager

) : ViewModel() {


    private val _toggleSwitchState =
        MutableStateFlow<NetworkResult<ToggleResponse>>(NetworkResult.Idle())
    val toggleSwitchState = _toggleSwitchState.asStateFlow()

    fun getCurrentUser() = tokenManager.getCurrentUser()


    @SuppressLint("CommitPrefEdits")
    fun toggleEmailNotification(){
        viewModelScope.launch {
            repo.toggleEmailNotification(token = tokenManager.getToken().toString())
                .collectLatest {
                    _toggleSwitchState.value = it
                    if (it is NetworkResult.Success){
                        it.result?.apply {
                            if (success){
                                val user = tokenManager.getCurrentUser()
                                user?.details?.email_updates = data.email_updates
                                user?.details?.notifications = data.notifications
                                preferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                                user?.let { it1 -> tokenManager.saveCurrentUser(it1) }
                            }
                        }
                    }
                }
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun togglePushNotification(){
        viewModelScope.launch {
            repo.togglePushNotification(token = tokenManager.getToken().toString())
                .collectLatest {
                    _toggleSwitchState.value = it
                    if (it is NetworkResult.Success){
                        it.result?.apply {
                            if (success){
                                val user =tokenManager.getCurrentUser()
                                user?.details?.email_updates = data.email_updates
                                user?.details?.notifications = data.notifications
                                preferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                                user?.let { it1 -> tokenManager.saveCurrentUser(it1) }
                            }
                        }
                    }
                }
        }
    }


}