package com.jeuxdevelopers.estatepie.ui.fragments.management.bottom.properties.filter

import androidx.lifecycle.ViewModel
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val propertiesRepo: ManagementPropertiesRepo
) : ViewModel() {

    var intentId: Int = 1
}