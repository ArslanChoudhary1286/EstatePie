package com.jeuxdevelopers.estatepie.ui.fragments.management.drawer.explore.viewall

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRecomRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.ViewAllRecomResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewAllViewModel @Inject constructor(
    private val viewAllRepo: ExploreRepo
)  : ViewModel() {

    val request = ViewAllRecomRequest()

    var recommendList = mutableListOf<ViewAllRecomResponse.Data.Recommended>()

    private val _viewAllUiState = MutableStateFlow<NetworkResult<ViewAllRecomResponse>>(
        NetworkResult.Idle())
    val viewAllUiState = _viewAllUiState.asStateFlow()

    fun getViewAllRecommendedList(
    ) {
        viewModelScope.launch {
            viewAllRepo.getViewAllRecommendedList(request).collectLatest { _viewAllUiState.value = it }
        }
    }
}