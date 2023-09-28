package com.jeuxdevelopers.estatepie.network

import com.jeuxdevelopers.estatepie.network.responses.auth.CheckSocialUserResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.DeleteAccountResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.ExploreRecommendedResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.LeaseTermResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.ViewAllRecomResponse
import com.jeuxdevelopers.estatepie.network.responses.management.auth.ManagementSignUpResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ForgotPasswordResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.network.responses.auth.ResendCodeResponse
import com.jeuxdevelopers.estatepie.network.responses.explore.AdsDetailResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.auth.TenantSignupResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.others.PropertyTypesResponse
import com.jeuxdevelopers.estatepie.network.responses.tenants.toggle.ToggleResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    // here will add all the API calls


//    @Headers("Accept:application/json","Content-Type:application/json")
    @Headers("Accept:application/json")
    @POST("/api/v1/tenant_register")
    suspend fun tenantSignUp(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String,
        @Query("device_type") device_type: String,
        @Query("password_confirmation") password_confirmation: String,
        @Query("device_token") device_token: String,
        @Query("first_name") first_name: String,
        @Query("last_name") last_name: String,
        @Query("phone") phone: String,
    ): Response<TenantSignupResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/landlord_register")
    suspend fun managementSignUp(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String,
        @Query("device_type") device_type: String,
        @Query("password_confirmation") password_confirmation: String,
        @Query("device_token") device_token: String,
        @Query("first_name") first_name: String,
        @Query("last_name") last_name: String,
        @Query("business_name") business_name: String,
        @Query("address") address: String,
    ): Response<ManagementSignUpResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("device_type") device_type: String,
    ): Response<LoginResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/social_login")
    suspend fun socialLogin(
        @Query("platform") platform: String,
        @Query("client_id") client_id: String,
        @Query("token") token: String,
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("device_token") device_token: String,
        @Query("device_type") device_type: String,
        @Query("role_id") role_id: Int,
        @Query("business_name") business_name: String,
    ): Response<LoginResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/check_social_user")
    suspend fun checkSocialUser(
        @Query("platform") platform: String,
        @Query("client_id") client_id: String,
    ): Response<CheckSocialUserResponse>

    @Headers("Accept:application/json")
    @POST("/api/v1/resend_code")
    suspend fun resendCode(
        @Query("verification_email") verification_email: String,
    ): Response<ResendCodeResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @POST("/api/v1/forget_password")
    suspend fun forgotPassword(
        @Query("email") email: String,
    ): Response<ForgotPasswordResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/get_explore_screen")
    suspend fun getExploreList (
        @Query("lat") lat: String,
        @Query("lng") lng: String,

        ): Response<ExploreRecommendedResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/view_all_recommend")
    suspend fun getViewAllRecommendedList(
        @Query("price_range_start") price_range_start: String,
        @Query("price_range_end") price_range_end: String,

    ): Response<ViewAllRecomResponse>

    @Headers("Accept:application/json","Content-Type:application/json")
    @GET("/api/v1/lease_terms")
    suspend fun leasingTerms(): Response<LeaseTermResponse>


    // tenant dashboard
//    @GET("/get_maintenance_request_list")
//    suspend fun getTenantMaintenanceRequestsList(
//        @Header("Authorization") bearToken:String,
//        @Header("Accept") accept:String,
//
//    )


    @GET("api/v1/property_types")
    suspend fun getPropertyTypes(
        @Header("Authorization") bearToken:String,
        @Header("Accept") accept:String,
    ):Response<PropertyTypesResponse>




    @POST("api/v1/update/user/password")
    suspend fun updateUserPassword(
        @Query("id") id:Int,
        @Query("old_pass") oldPassword:String,
        @Query("new_pass") newPassword:String
    ):Response<ResponseBody>


    @POST("api/v1/push_notification_toggle")
    suspend fun togglePushNotification(
        @Header("Authorization") bearToken:String,
        @Header("Accept") accept:String,
    ):Response<ToggleResponse>

    @POST("api/v1/email_notification_toggle")
    suspend fun toggleEmailNotification(
        @Header("Authorization") bearToken:String,
        @Header("Accept") accept:String,
    ):Response<ToggleResponse>

    @Multipart
    @POST("api/v1/update_user")
    suspend fun updateTenantProfile(
        @Header("Authorization") bearToken:String,
        @Header("Accept") accept:String,
    ):Response<LoginResponse>
}