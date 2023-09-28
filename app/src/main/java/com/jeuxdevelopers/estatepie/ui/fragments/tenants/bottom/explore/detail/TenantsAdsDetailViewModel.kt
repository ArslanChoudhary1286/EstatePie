package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.detail

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.models.chat.inbox.InboxModel
import com.jeuxdevelopers.estatepie.network.requests.explore.MarkToFavoriteRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.properties.PropertyDetailRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.MarkToFavoriteResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsAdsDetailViewModel @Inject constructor(
    private val exploreRepo: ExploreRepo,
    private val tenantsPropertiesRepo: TenantsPropertiesRepo,
    private val sharedPreferences: SharedPreferences
)  : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager

    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

    val inboxModel  = InboxModel()

    var lasingTerms :String = "N/A"

    var lasingTitle :String = "Leasing Terms"

    val request = ViewAllRequest()
    val markToFavoriteRequest = MarkToFavoriteRequest()

    val propertiesDetailsRequest = PropertyDetailRequest()

    private val _leasingTermsUiState = MutableStateFlow<NetworkResult<LeaseTermResponse>>(
        NetworkResult.Idle())
    val leasingTermsUiState = _leasingTermsUiState.asStateFlow()

    private val _markToFavoriteUiState = MutableStateFlow<NetworkResult<MarkToFavoriteResponse>>(
        NetworkResult.Idle())
    val markToFavoriteUiState = _markToFavoriteUiState.asStateFlow()

    private val _propertiesDetailsUiState = MutableSharedFlow<NetworkResult<AdsDetailResponse>>()
    val propertiesDetailsUiState = _propertiesDetailsUiState.asSharedFlow()

    fun leasingTerms(
    ) {
        viewModelScope.launch {
            exploreRepo.leasingTerms().collectLatest {
                _leasingTermsUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.let { user ->
                        // saved in share prefs as well if the user successfully sign up and has data
//                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                    }
                }
            }
        }
    }

    fun markToFavorite(
    ) {
        viewModelScope.launch {
            exploreRepo.markToFavorite(markToFavoriteRequest).collectLatest {
                _markToFavoriteUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.let { user ->
                        // saved in share prefs as well if the user successfully sign up and has data
//                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                    }
                }
            }
        }
    }

    fun getPropertiesDetails(
    ) {
        viewModelScope.launch {
            tenantsPropertiesRepo.getPropertiesDetails(propertiesDetailsRequest).collectLatest {
                _propertiesDetailsUiState.emit(it)
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