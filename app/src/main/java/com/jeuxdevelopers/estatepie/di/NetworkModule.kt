package com.jeuxdevelopers.estatepie.di

import android.content.Context
import android.content.SharedPreferences
import com.jeuxdevelopers.estatepie.network.ApiService
import com.jeuxdevelopers.estatepie.network.AuthInterceptor
import com.jeuxdevelopers.estatepie.network.RemoteDataSource
import com.jeuxdevelopers.estatepie.network.ApiAuthService
import com.jeuxdevelopers.estatepie.repos.Plans.PlansRepo
import com.jeuxdevelopers.estatepie.repos.Plans.PlansRepoImpl
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepo
import com.jeuxdevelopers.estatepie.repos.auth.AuthRepoImpl
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepo
import com.jeuxdevelopers.estatepie.repos.explore.ExploreRepoImpl
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepo
import com.jeuxdevelopers.estatepie.repos.managemnet.ManagementPropertiesRepoImpl
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepo
import com.jeuxdevelopers.estatepie.repos.tenants.TenantsPropertiesRepoImpl
import com.jeuxdevelopers.estatepie.utils.AppConsts.BASE_API_URL
import com.jeuxdevelopers.estatepie.utils.extensions.ResourceUtility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {


    // retrofit
    @Provides
    fun provideHTTPClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    fun provideConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
//            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)

    }

    @Provides
    fun provideApiService(retrofitBuilder: Retrofit.Builder): ApiService =
        retrofitBuilder.build().create(ApiService::class.java)

    @Provides
    fun provideApiAuthService(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) : ApiAuthService =
        retrofitBuilder.client(okHttpClient).build().create(ApiAuthService::class.java)


    @Provides
    fun provideAuthRepo(
        remoteDataSource: RemoteDataSource,
    ): AuthRepo = AuthRepoImpl(
        remoteDataSource
    )

    @Provides
    fun provideExploreRepo(
        remoteDataSource: RemoteDataSource,
    ): ExploreRepo = ExploreRepoImpl(
        remoteDataSource
    )

    @Provides
    fun provideManagementPropertiesRepo(
        remoteDataSource: RemoteDataSource,
    ): ManagementPropertiesRepo = ManagementPropertiesRepoImpl(
        remoteDataSource
    )

    @Provides
    fun provideTenantsPropertiesRepo(
        remoteDataSource: RemoteDataSource,
    ): TenantsPropertiesRepo = TenantsPropertiesRepoImpl(
        remoteDataSource
    )

    @Provides
    fun providePlansRepo(
        remoteDataSource: RemoteDataSource,
    ): PlansRepo = PlansRepoImpl(
        remoteDataSource
    )

    // Provides Shared Preferences.
    @Provides
    @Singleton
    fun providesPreferences(

        @ApplicationContext context: Context

    ): SharedPreferences {

        return context.getSharedPreferences(
            context.packageName, Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun providesResources(

        @ApplicationContext context: Context

    ): ResourceUtility {

        return ResourceUtility(context)
    }

}