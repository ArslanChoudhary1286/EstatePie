package com.jeuxdevelopers.estatepie.repos.Plans

import com.jeuxdevelopers.estatepie.network.requests.plans.CreateCustomerRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.MakeSubscriptionsRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.PayBillRequest
import com.jeuxdevelopers.estatepie.network.requests.plans.UpdateCustomerRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.plans.*
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow

interface PlansRepo {

    fun getSubscriptionPlans() : Flow<NetworkResult<SubscriptionPlansResponse>>

    fun makeSubscriptions(request: MakeSubscriptionsRequest) : Flow<NetworkResult<LoginResponse>>

    fun getCardDetail() : Flow<NetworkResult<CardDetailResponse>>

    fun addCustomerCard(request: CreateCustomerRequest) : Flow<NetworkResult<CreateCustomerResponse>>

    fun updateCustomerCard(id: String, request: UpdateCustomerRequest) : Flow<NetworkResult<UpdateCustomerResponse>>

    fun payBill(request: PayBillRequest) : Flow<NetworkResult<PayBillResponse>>

}