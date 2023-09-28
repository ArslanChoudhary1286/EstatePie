package com.jeuxdevelopers.estatepie.repos.auth

import com.jeuxdevelopers.estatepie.network.requests.auth.DeleteAccountRequest
import com.jeuxdevelopers.estatepie.network.requests.management.auth.ManagementSignupRequest
import com.jeuxdevelopers.estatepie.network.requests.auth.ForgotPasswordRequest
import com.jeuxdevelopers.estatepie.network.requests.auth.ResendCodeRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.CheckSocialUserRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.SocialLoginRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantLoginRequest
import com.jeuxdevelopers.estatepie.network.requests.tenant.auth.TenantSignupRequest
import com.jeuxdevelopers.estatepie.network.responses.auth.CheckSocialUserResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.DeleteAccountResponse
import com.jeuxdevelopers.estatepie.network.responses.management.auth.ManagementSignUpResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ForgotPasswordResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ResendCodeResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.auth.TenantSignupResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody


interface AuthRepo {

    fun signupNewTenant(request: TenantSignupRequest): Flow<NetworkResult<TenantSignupResponse>>
    fun signupNewManagement(request: ManagementSignupRequest): Flow<NetworkResult<ManagementSignUpResponse>>

    fun loginUser(request: TenantLoginRequest): Flow<NetworkResult<LoginResponse>>

    fun socialLogin(request: SocialLoginRequest): Flow<NetworkResult<LoginResponse>>

    fun checkSocialUser(request: CheckSocialUserRequest): Flow<NetworkResult<CheckSocialUserResponse>>

    fun resendCode(request: ResendCodeRequest): Flow<NetworkResult<ResendCodeResponse>>

    fun forgotPassword(request: ForgotPasswordRequest): Flow<NetworkResult<ForgotPasswordResponse>>
    fun deleteUserAccount(request: DeleteAccountRequest): Flow<NetworkResult<DeleteAccountResponse>>

    fun updatePassword(userId:Int,oldPassword:String,newPassword:String) : Flow<NetworkResult<ResponseBody>>

//    fun updateProfile(update: TenantUpdateRequest):Flow<NetworkResult<TenantLoginResponse>>

}