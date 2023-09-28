package com.jeuxdevelopers.estatepie.ui.activities.managment

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.jeuxdevelopers.estatepie.network.requests.auth.DeleteAccountRequest
import com.jeuxdevelopers.estatepie.network.requests.auth.ForgotPasswordRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.DeleteAccountResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ForgotPasswordResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainManagementViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    @Inject
    lateinit var tokenManager: TokenManager
    private val _splashUiState = MutableStateFlow(false)
    val splashUiState = _splashUiState.asStateFlow()


    fun getCurrentUser(): LoginResponse.Data.User? {
        return tokenManager.getCurrentUser()//usersRepo.getCurrentUserId()
    }

    private val _planUpdateState = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle())
//    private val _planUpdateState = MutableStateFlow<NetworkResult<LoginResponse>>(NetworkResult.Idle())
    val planUpdateState = _planUpdateState.asStateFlow()

    val request = DeleteAccountRequest()

    private val _deleteAccountUiState = MutableStateFlow<NetworkResult<DeleteAccountResponse>>(NetworkResult.Idle())
    val deleteAccountUiState = _deleteAccountUiState.asStateFlow()

    fun deleteUserAccount(
    ) {
        viewModelScope.launch {
            authRepo.deleteUserAccount(request).collectLatest {
                _deleteAccountUiState.value = it
                if (it is NetworkResult.Success){
                    it.result?.data?.let { user ->

                        tokenManager.saveCurrentUser(null)
                        tokenManager.saveToken("")
                        // saved in share prefs as well if the user successfully sign up and has data
//                        sharedPreferences.edit().putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
                    }
                }
            }
        }
    }


    //Tod here we will implement subscription payment system
    // billing client purchase methods---------------------------------------

    private lateinit var billingClient: BillingClient
    fun initBillingClient(context: Context) {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    private var _isConnectedToPayment = false

    // listener for purchase updates
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Timber.d("Purchase success: message -> " + billingResult.debugMessage + " ")
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Timber.d("user cancel the billing flow: message -> " + billingResult.debugMessage)
        } else {
            Timber.d("some other error exist -> :" + billingResult.debugMessage)
            // Handle any other error codes.
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        Timber.d("handlePurchase: purchase -> " + purchase.packageName)
        viewModelScope.launch {
            //TOD: initiate consume in the case of consumable item e.g Subscription item
            val consumeParams =
                ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            val consumeResult = withContext(Dispatchers.IO) {
                billingClient.consumePurchase(consumeParams)
            }
            Timber.d("handlePurchase: consume results -> " + consumeResult.billingResult.debugMessage)


            // perform acknowledgement in case of purchased item .e.g In app purchase
            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged) {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                    val ackPurchaseResult = withContext(Dispatchers.IO) {
                        billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                    }
                    Timber.d("handlePurchase: " + ackPurchaseResult.debugMessage)

//                    userRepo.updatePlan(PlanType.PREMIUM).collectLatest {
//                        _planUpdateState.value = it
//                    }

                } else {
                    Timber.d("handlePurchase: purchase already acknowledged")
//                    userRepo.updatePlan(PlanType.PREMIUM).collectLatest {
//                        _planUpdateState.value = it
//                    }

                }
            } else {
                Timber.d("handlePurchase: not purchased state -> " + purchase.purchaseState)
            }

        }
    }

    fun startBillingConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Timber.d("onBillingServiceDisconnected: unable to connect, try again in next request")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        _isConnectedToPayment = true
                        Timber.d("onBillingSetupFinished: The billing is ready. You can purchase here")
                    }
                    BillingClient.BillingResponseCode.USER_CANCELED -> {
                        Timber.d("Connection -> Response Code -> USER_CANCELED")
                    }
                    else -> {
                        Timber.d("onBillingSetupFinished: error -> " + billingResult.debugMessage)
                    }
                }
            }

        })
    }

    fun purchaseWithSubscription(purchase: String, activity: Activity) {
        if (_isConnectedToPayment) {
            viewModelScope.launch {
                val skuList = ArrayList<String>()
                skuList.add(purchase)

                val productList = listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(purchase.lowercase())
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )

                val params = QueryProductDetailsParams.newBuilder().setProductList(productList)
                // leverage querySkuDetails Kotlin extension function
                val productDetailsResult = withContext(Dispatchers.IO) {
                    billingClient.queryProductDetails(params.build())
                }

                Timber.d("Product details list size -> ${productDetailsResult.productDetailsList?.size}")
                Timber.d("Product details result -> " + productDetailsResult.billingResult.responseCode)
                Timber.d("Product details result msg -> " + productDetailsResult.billingResult.debugMessage)
                if (productDetailsResult.productDetailsList != null && productDetailsResult.productDetailsList!!.isNotEmpty()) {
                    val offerToken =
                        productDetailsResult.productDetailsList?.get(0)?.subscriptionOfferDetails?.get(
                            0
                        )?.offerToken!!
                    val productDetailsParamsList = listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetailsResult.productDetailsList?.get(0)!!)
                            .setOfferToken(offerToken)
                            .build()
                    )
                    val flowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()
                    val responseCode =
                        billingClient.launchBillingFlow(activity, flowParams).responseCode
                    Timber.d("purchaseWithInAppPurchase: launch response code -> $responseCode")
                } else {
                    Timber.d("purchaseWithInAppPurchase: sku results are empty or null")
                }
            }
        } else {
            Timber.d("purchaseWithInAppPurchase: unable to connect, please try again")
        }


    }

    fun updateArticlesCount() {
        viewModelScope.launch {
//            userRepo.updateArticlesCount(reelsRepo.getReelsCount()).collectLatest {
//                _updateArticlesCount.value = it
//            }
        }
    }

}