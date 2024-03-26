package com.itskidan.kinostock.application

import DaggerRemoteComponent
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.itskidan.core.CoreProvidersFactory
import com.itskidan.kinostock.BuildConfig
import com.itskidan.kinostock.R
import com.itskidan.kinostock.di.AppComponent
import com.itskidan.kinostock.di.DaggerAppComponent
import com.itskidan.kinostock.di.AppContextComponent
import com.itskidan.kinostock.di.modules.DomainModule
import com.itskidan.kinostock.lifecycle.LifecycleObserver
import com.itskidan.kinostock.utils.Constants
import timber.log.Timber

class App : Application() {
    lateinit var sharedPreferences: SharedPreferences
    val lifecycleObserver = LifecycleObserver()
    lateinit var dagger: AppComponent
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        instance = this
        val remoteProvider = DaggerRemoteComponent.create()
        val appProvider = AppContextComponent.create(this)
        val databaseProvider = CoreProvidersFactory.createDatabaseBuilder(appProvider)
        dagger = DaggerAppComponent.builder()
            .remoteProvider(remoteProvider)
            .appProvider(appProvider)
            .databaseProvider(databaseProvider)
            .domainModule(DomainModule(this))
            .build()
        sharedPreferences =
            instance.applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        createNotificationChannel()
    }

    companion object {
        lateinit var instance: App
            private set
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            notificationChannel =
                NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_ID,
                    name,
                    importance
                )
            notificationChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}