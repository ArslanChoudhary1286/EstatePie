package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.viewall

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
class TenantsViewAllViewModel @Inject constructor(
    private val viewAllRepo: ExploreRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    val request = ViewAllRecomRequest()

    var recommendList = mutableListOf<ViewAllRecomResponse.Data.Recommended>()

    private val _viewAllUiState = MutableStateFlow<NetworkResult<ViewAllRecomResponse>>(
        NetworkResult.Idle())
    val viewAllUiState = _viewAllUiState.asStateFlow()

    fun getViewAllRecommendedList(
    ) {
        viewModelScope.launch {
            viewAllRepo.getViewAllRecommendedList(request).collectLatest {
                _viewAllUiState.value = it
                if (it is NetworkResult.Success){
//                    it.result?.data?.let { user ->
////                        Log.d("data","datais=" + user.last_page);
//                        // saved in share prefs as well if the user successfully sign up and has data
////                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
//                    }
                }
            }
        }
    }
}