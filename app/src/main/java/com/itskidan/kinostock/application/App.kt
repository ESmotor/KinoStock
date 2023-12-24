package com.itskidan.kinostock.application

import android.app.Application
import com.itskidan.kinostock.BuildConfig
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.lifecycle.LifecycleObserver
import timber.log.Timber

class App : Application() {
    lateinit var interactor: Interactor
    lateinit var repository: MainRepository

    val lifecycleObserver = LifecycleObserver()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
        repository = MainRepository()
        interactor = Interactor(repository)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}