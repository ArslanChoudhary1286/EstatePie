package com.jeuxdevelopers.estatepie.repos.Plans

import com.jeuxdevelopers.estatepie.network.BaseApiResponse
import com.jeuxdevelopers.estatepie.network.RemoteDataSource
import com.jeuxdevelopers.estatepie.network.requests.plans.CreateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.MakeSubscriptionsRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.PayBillRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.UpdateCustomerRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.plans.*
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PlansRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : PlansRepo, BaseApiResponse() {


    override fun getSubscriptionPlans(): Flow<NetworkResult<SubscriptionPlansResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getSubscriptionPlans() })
        }.flowOn(Dispatchers.IO)

    override fun makeSubscriptions(request: MakeSubscriptionsRequest): Flow<NetworkResult<LoginResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.makeSubscriptions(request) })
        }.flowOn(Dispatchers.IO)

    override fun getCardDetail(): Flow<NetworkResult<CardDetailResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getCardDetail() })
        }.flowOn(Dispatchers.IO)

    override fun addCustomerCard(request: CreateCustomerRequest): Flow<NetworkResult<CreateCustomerResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.addCustomerCard(request) })
        }.flowOn(Dispatchers.IO)

    override fun updateCustomerCard(id: String, request: UpdateCustomerRequest): Flow<NetworkResult<UpdateCustomerResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.updateCustomerCard(id, request) })
        }.flowOn(Dispatchers.IO)

    override fun payBill(request: PayBillRequest): Flow<NetworkResult<PayBillResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.payBill(request) })
        }.flowOn(Dispatchers.IO)


}