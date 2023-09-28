package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.exploreads

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.explore.ExploreRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementExploreAdsViewModel @Inject constructor(
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
            }
        }
    }
}