package com.itskidan.kinostock.application

import android.app.Application
import com.itskidan.kinostock.BuildConfig
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.TmdbApi
import com.itskidan.kinostock.lifecycle.LifecycleObserver
import com.itskidan.kinostock.utils.ApiConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var interactor: Interactor
    lateinit var repository: MainRepository
    lateinit var retrofitService: TmdbApi
    val lifecycleObserver = LifecycleObserver()

    // Create a custom client
    val okHttpClient = OkHttpClient.Builder()
        // Configure timeouts for slow Internet
        .callTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        // Add a logger
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    val retrofit = Retrofit.Builder()
        // Specify the base URL from constants
        .baseUrl(ApiConstants.BASE_URL)
        // Add a converter
        .addConverterFactory(GsonConverterFactory.create())
        // Add a custom client
        .client(okHttpClient)
        .build()


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        retrofitService = retrofit.create(TmdbApi::class.java)
        instance = this
        repository = MainRepository()
        interactor = Interactor(repository, retrofitService)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}