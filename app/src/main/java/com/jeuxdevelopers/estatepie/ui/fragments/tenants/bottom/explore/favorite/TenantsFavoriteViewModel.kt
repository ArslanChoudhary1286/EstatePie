package com.jeuxdevelopers.estatepie.ui.fragments.tenants.bottom.explore.favorite

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.FavoritesResponse
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantsFavoriteViewModel @Inject constructor(
    private val exploreRepo: ExploreRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _favoritesUiState = MutableStateFlow<NetworkResult<FavoritesResponse>>(
        NetworkResult.Idle())
    val favoritesUiState = _favoritesUiState.asStateFlow()


    fun getFavorites(
    ) {
        viewModelScope.launch {
            exploreRepo.getAllFavorites().collectLatest {
                _favoritesUiState.value = it
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