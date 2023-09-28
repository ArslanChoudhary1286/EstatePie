package com.jeuxdevelopers.estatepie.repos.auth

import com.jeuxdevelopers.estatepie.network.BaseApiResponse
import com.jeuxdevelopers.estatepie.network.RemoteDataSource
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : AuthRepo, BaseApiResponse() {

    override fun signupNewTenant(request: TenantSignupRequest): Flow<NetworkResult<TenantSignupResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.signUpTenantUser(request) })
        }.flowOn(Dispatchers.IO)

    override fun signupNewManagement(request: ManagementSignupRequest): Flow<NetworkResult<ManagementSignUpResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.signUpManagementUser(request) })
        }.flowOn(Dispatchers.IO)

    override fun loginUser(request: TenantLoginRequest): Flow<NetworkResult<LoginResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.loginUser(request) })
        }.flowOn(Dispatchers.IO)

    override fun socialLogin(request: SocialLoginRequest): Flow<NetworkResult<LoginResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.socialLogin(request) })
        }.flowOn(Dispatchers.IO)

    override fun checkSocialUser(request: CheckSocialUserRequest): Flow<NetworkResult<CheckSocialUserResponse>> =
    flow {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.checkSocialUser(request) })
    }.flowOn(Dispatchers.IO)

    override fun resendCode(request: ResendCodeRequest): Flow<NetworkResult<ResendCodeResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.resendCode(request) })
        }.flowOn(Dispatchers.IO)

    override fun forgotPassword(request: ForgotPasswordRequest): Flow<NetworkResult<ForgotPasswordResponse>> =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.forgotPassword(request) })
        }.flowOn(Dispatchers.IO)

    override fun deleteUserAccount(request: DeleteAccountRequest): Flow<NetworkResult<DeleteAccountResponse>>  =
        flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.deleteAccount(request) })
        }.flowOn(Dispatchers.IO)


    override fun updatePassword(
        userId: Int,
        oldPassword: String,
        newPassword: String
    ): Flow<NetworkResult<ResponseBody>> = flow<NetworkResult<ResponseBody>> {
        emit(NetworkResult.Loading())
        emit(safeApiCall { remoteDataSource.updatePassword(userId, oldPassword, newPassword) })
    }.flowOn(Dispatchers.IO)

//    override fun updateProfile(update: TenantUpdateRequest): Flow<NetworkResult<TenantLoginResponse>> {
//        TODO("Not yet implemented")
//    }

}