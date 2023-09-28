package com.jeuxdevelopers.estatepie.ui.fragments.explore.filter

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdsFilterViewModel @Inject constructor(
    private val viewAllRepo: ExploreRepo,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

}