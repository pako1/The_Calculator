package com.example.calc

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class TheCalculator : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {  }


        initializeTimber()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}