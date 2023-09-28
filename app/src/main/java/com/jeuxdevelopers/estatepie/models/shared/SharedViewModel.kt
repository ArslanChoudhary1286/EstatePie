package com.jeuxdevelopers.estatepie.models.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    //  Residential search

    private var _residentialSearch = MutableLiveData("")
    val residentialSearch : LiveData<String> = _residentialSearch

    private var _sortResidential = MutableLiveData("")
    val sortResidential : LiveData<String> = _sortResidential

    fun searchResidential(newSearch: String){
        _residentialSearch.value = newSearch
    }

    fun sortResidential(sort: String){
        _sortResidential.value = sort
    }

    // commercial search

    private var _commercialSearch = MutableLiveData("")
    val commercialSearch : LiveData<String> = _commercialSearch

    private var _sortCommercial = MutableLiveData("")
    val sortCommercial : LiveData<String> = _sortCommercial

    fun searchCommercial(newSearch: String){
        _commercialSearch.value = newSearch
    }

    fun sortCommercial(sort: String){
        _sortCommercial.value = sort
    }

    // Ads search

    private var _adsSearch = MutableLiveData("")
    val adsSearch : LiveData<String> = _adsSearch

    private var _sortAds = MutableLiveData("")
    val sortAds : LiveData<String> = _sortAds

    fun searchAds(newSearch: String){
        _adsSearch.value = newSearch
    }

    fun sortAds(sort: String){
        _sortAds.value = sort
    }

    private var _refresh = MutableLiveData(false)
    val refresh : LiveData<Boolean> = _refresh

    fun refreshView(isRefresh: Boolean){
        _refresh.value = isRefresh
    }

}