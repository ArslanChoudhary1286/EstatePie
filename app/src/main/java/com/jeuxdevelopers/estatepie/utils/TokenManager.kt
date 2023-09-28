package com.jeuxdevelopers.estatepie.utils

import android.content.Context
import com.google.gson.Gson
import com.jeuxdevelopers.estatepie.network.responses.auth.LoginResponse
import com.jeuxdevelopers.estatepie.utils.AppConsts.LAT
import com.jeuxdevelopers.estatepie.utils.AppConsts.LNG
import com.jeuxdevelopers.estatepie.utils.AppConsts.PREF_TOKEN_FILE
import com.jeuxdevelopers.estatepie.utils.AppConsts.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREF_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveCurrentUser(user: LoginResponse.Data.User?) {
        val editor = prefs.edit()
        editor.putString(AppConsts.PREF_USER_MODEL, Gson().toJson(user))
        editor.apply()
    }

    fun getCurrentUser(): LoginResponse.Data.User? {
        val json = prefs.getString(AppConsts.PREF_USER_MODEL, null)
        Timber.d("CurrentUser -> json : $json")
        return Gson().fromJson(
            json,
            LoginResponse.Data.User::class.java
        )
    }

    fun saveToken(token: String) {

        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()

    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveLatitude(lat: String){
        val editor = prefs.edit()
        editor.putString(LAT, lat)
        editor.apply()
    }

    fun getLat(): String? {
        return prefs.getString(LAT, null)
    }

    fun saveLongitude(lng: String){
        val editor = prefs.edit()
        editor.putString(LNG, lng)
        editor.apply()
    }

    fun getLng(): String? {
        return prefs.getString(LNG, null)
    }


}