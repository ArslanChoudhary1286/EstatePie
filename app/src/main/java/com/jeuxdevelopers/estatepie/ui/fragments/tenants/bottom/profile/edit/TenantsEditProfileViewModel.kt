package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.profile.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.jeuxdevelopers.estatepie.network.requests.management.settings.UpdateUserProfileRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.management.settings.UpdateUserProfileResponse
import com.jeuxdevelopers.estatepie.network.responses.management.settings.UserProfileResponse
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsEditProfileViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo,
    private val tokenManager: TokenManager
) : ViewModel() {

     val user = tokenManager.getCurrentUser()

    fun getCurrentUser() = tokenManager.getCurrentUser()


    var imageUri: Uri? = null

    var imageBitmap: Bitmap? = null

    var placeCity: String? = null

    var placeCountry: String? = null

    var placeLocation: LatLng? = null

    val updateUserProfileRequest = UpdateUserProfileRequest()


    private val _updateUserProfileState =
        MutableStateFlow<NetworkResult<UpdateUserProfileResponse>>(NetworkResult.Idle())

    val updateUserProfileState = _updateUserProfileState.asStateFlow()


    fun updateUserProfile(
    ) {
        viewModelScope.launch {
            propertiesRepo.updateUserProfile(updateUserProfileRequest).collectLatest {
                _updateUserProfileState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.details.let { data ->

                        var loginResponse = tokenManager.getCurrentUser()

                        if (data?.address == null){
                            loginResponse?.details?.address = ""
                        }else{
                            loginResponse?.details?.address = data.address
                        }

                        loginResponse?.details?.email_updates = data!!.email_updates
                        loginResponse?.details?.image = data.image
                        loginResponse?.details?.image_url = data.image_url
                        loginResponse?.details?.last_name = data.last_name
                        loginResponse?.details?.first_name = data.first_name
                        loginResponse?.details?.phone = data.phone

                        tokenManager.saveCurrentUser(loginResponse)

                    }
                }}
        }
    }

}