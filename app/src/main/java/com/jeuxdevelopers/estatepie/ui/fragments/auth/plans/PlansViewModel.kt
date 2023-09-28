package com.jeuxdevelopers.estatepie.ui.fragments.auth.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeuxdevelopers.estatepie.network.requests.plans.MakeSubscriptionsRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.plans.SubscriptionPlansResponse
import com.jeuxdevelopers.estatepie.repos.Plans.PlansRepo
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import com.jeuxdevelopers.estatepie.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlansViewModel @Inject constructor(
    private val plansRepo: PlansRepo,
    private val tokenManager: TokenManager
) : ViewModel() {

    var checkId: Int = 0

    var planName: String = ""

    var pm: String = ""
    var py: String = ""
    var sm: String = ""
    var sy: String = ""
    var fm: String = ""
    var fy: String = ""

    fun getCurrentUser() = tokenManager.getCurrentUser()

    val request = MakeSubscriptionsRequest()

    private val _subscriptionPlansUiState = MutableStateFlow<NetworkResult<SubscriptionPlansResponse>>(
        NetworkResult.Idle())
    val subscriptionPlansUiState = _subscriptionPlansUiState.asStateFlow()

    private val _makeSubscriptionUiState = MutableSharedFlow<NetworkResult<LoginResponse>>()
    val makeSubscriptionUiState = _makeSubscriptionUiState.asSharedFlow()

    fun getSubscriptionPlans(
    ) {
        viewModelScope.launch {
            plansRepo.getSubscriptionPlans().collectLatest {
                _subscriptionPlansUiState.value = it
            }
        }
    }

    fun makeSubscriptions(
    ) {
        viewModelScope.launch {
            plansRepo.makeSubscriptions(request).collectLatest {
                _makeSubscriptionUiState.emit(it)
                if (it is NetworkResult.Success){
                    it.result?.data?.user?.let { user ->
                        // saved in share prefs as well if the user successfully sign up and has data
                        tokenManager.saveCurrentUser(user)
                        tokenManager.saveToken(user.access_token)

                    }
                }
            }
        }
    }

}