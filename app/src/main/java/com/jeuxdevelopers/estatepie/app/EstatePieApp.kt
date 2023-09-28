package com.jeuxdevelopers.estatepie.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jeuxdevelopers.estatepie.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class EstatePieApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            FirebaseApp.initializeApp(this)
        }
    }
}