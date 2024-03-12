package com.itskidan.kinostock.application

import DaggerRemoteComponent
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.itskidan.kinostock.BuildConfig
import com.itskidan.kinostock.di.AppComponent
import com.itskidan.kinostock.di.DaggerAppComponent
import com.itskidan.kinostock.di.modules.DatabaseModule
import com.itskidan.kinostock.di.modules.DomainModule
import com.itskidan.kinostock.lifecycle.LifecycleObserver
import timber.log.Timber

class App : Application() {
    lateinit var sharedPreferences: SharedPreferences
    val lifecycleObserver = LifecycleObserver()
    lateinit var dagger : AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        val remoteProvider = DaggerRemoteComponent.create()
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
        sharedPreferences = instance.applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}