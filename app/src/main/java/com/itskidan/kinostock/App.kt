package com.itskidan.kinostock

import android.app.Application
import timber.log.Timber

class App : Application() {

    val lifecycleObserver = LifecycleObserver()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}