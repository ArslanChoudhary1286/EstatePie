package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile

import androidx.lifecycle.ViewModel
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TenantProfileViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    fun getCurrentUser() = tokenManager.getCurrentUser()
}