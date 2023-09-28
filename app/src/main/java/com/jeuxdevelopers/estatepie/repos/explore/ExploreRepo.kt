package com.jeuxdevelopers.estatepie.repos.explore

import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ExploreRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.MarkToFavoriteRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRecomRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.FavoritesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ExploreRepo {

    fun getExploreList(request: ExploreRequest) : Flow<NetworkResult<ExploreRecommendedResponse>>

    fun getViewAllRecommendedList(request: ViewAllRecomRequest): Flow<NetworkResult<ViewAllRecomResponse>>

    fun leasingTerms(): Flow<NetworkResult<LeaseTermResponse>>

//    fun getAdsProperties(): Flow<NetworkResult<AdsPropertyResponse>>

//    fun getAdsDetail(request: AdsDetailRequest): Flow<NetworkResult<AdsDetailResponse>>

    fun markToFavorite(request: MarkToFavoriteRequest): Flow<NetworkResult<MarkToFavoriteResponse>>

    fun getAllFavorites(): Flow<NetworkResult<FavoritesResponse>>
}