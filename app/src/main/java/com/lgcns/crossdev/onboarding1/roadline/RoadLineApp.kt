package com.lgcns.crossdev.onboarding1.roadline

import android.app.Application
import com.lgcns.crossdev.onboarding1.data.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RoadLineApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}