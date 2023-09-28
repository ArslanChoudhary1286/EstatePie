package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.network.requests.explore.ExploreRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
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
class TenantsExplorViewModel @Inject constructor(
    private val exploreRepo: ExploreRepo,
    private val sharedPreferences: SharedPreferences,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _exploreUiState = MutableStateFlow<NetworkResult<ExploreRecommendedResponse>>(
        NetworkResult.Idle())
    val exploreUiState = _exploreUiState.asStateFlow()

    var request = ExploreRequest()


    fun getRecommendedExploreList(
    ) {
        request.lat = tokenManager.getLat().toString()
        request.lng = tokenManager.getLng().toString()

        viewModelScope.launch {
            exploreRepo.getExploreList(request).collectLatest {
                _exploreUiState.value = it
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