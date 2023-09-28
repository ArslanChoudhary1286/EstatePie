package com.jeuxdevelopers.estatepie.repos.explore

import com.jeuxdevelopers.estatepie.network.BaseApiResponse
import com.jeuxdevelopers.estatepie.network.RemoteDataSource
import com.jeuxdevelopers.estatepie.network.requests.explore.AdsDetailRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ExploreRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.MarkToFavoriteRequest
import com.jeuxdevelopers.estatepie.network.requests.explore.ViewAllRecomRequest
import com.jeuxdevelopers.estatepie.network.responses.explore.*
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.AdsPropertyResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.explore.FavoritesResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExploreRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) :ExploreRepo, BaseApiResponse() {

    override fun getExploreList(request: ExploreRequest): Flow<NetworkResult<ExploreRecommendedResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getExploreList(request) })
        }.flowOn(Dispatchers.IO)

    override fun getViewAllRecommendedList(request: ViewAllRecomRequest): Flow<NetworkResult<ViewAllRecomResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getViewAllRecommendedList(request) })
        }.flowOn(Dispatchers.IO)

    override fun leasingTerms(): Flow<NetworkResult<LeaseTermResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.leasingTerms() })
        }.flowOn(Dispatchers.IO)

//    override fun getAdsProperties(): Flow<NetworkResult<AdsPropertyResponse>> =
//        flow {
//            emit(NetworkResult.Loading())
//            emit(safeApiCall { remoteDataSource.getAdsProperties() })
//        }.flowOn(Dispatchers.IO)

//    override fun getAdsDetail(request: AdsDetailRequest): Flow<NetworkResult<AdsDetailResponse>> =
//        flow {
//            emit(NetworkResult.Loading())
//            emit(safeApiCall { remoteDataSource.getAdsDetail(request) })
//        }.flowOn(Dispatchers.IO)

    override fun markToFavorite(request: MarkToFavoriteRequest): Flow<NetworkResult<MarkToFavoriteResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.markToFavorite(request) })
        }.flowOn(Dispatchers.IO)

    override fun getAllFavorites(): Flow<NetworkResult<FavoritesResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getAllFavorites() })
        }.flowOn(Dispatchers.IO)

}