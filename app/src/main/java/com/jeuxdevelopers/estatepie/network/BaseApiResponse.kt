package com.jeuxdevelopers.estatepie.network

import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.network.responses.error.ErrorResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.auth.TenantSignupResponse
import com.jeuxdevelopers.estatepie.sealed.NetworkResult
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                Timber.d("SafeAPICall : body -> ${response.body().toString()}")
                body?.let {
                    return NetworkResult.Success(body)
                }
            }

            val errorMsg = response.errorBody()?.string()
            response.errorBody()?.close()
            Timber.d("SafeAPICall2 : errorBody -> ${errorMsg}  code -> ${response.code()}")
            val errorResponse = Gson().fromJson(errorMsg,ErrorResponse::class.java)
            return error("${errorResponse.message}",response.body())

        } catch (e: Exception) {
            return error("Api call failed ${e.message ?: e.toString()}")
        }
    }

    private fun <T> error(errorMessage: String, body: T? = null): NetworkResult<T> =
        NetworkResult.Error("$errorMessage",body)

}
