package com.itskidan.kinostock.di.modules

import com.itskidan.kinostock.BuildConfig
import com.itskidan.kinostock.domain.TmdbApi
import com.itskidan.kinostock.utils.ApiConstants
import dagger.Binds
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [BindOkHttpClient::class, BindRetrofit::class])
class RemoteModule {
    @Provides
    @Singleton
    fun provideTmdbApi(retrofitProvider: RetrofitProvider): TmdbApi {
        return retrofitProvider.provideRetrofit(retrofitProvider.provideOkHttpClient())
            .create(TmdbApi::class.java)
    }
}

@Module(includes = [BindOkHttpClient::class])
interface BindRetrofit {
    @Binds
    @Singleton
    fun bindRetrofitProvider(implementation: RetrofitProviderImpl): RetrofitProvider
}

@Module
interface BindOkHttpClient {
    @Binds
    @Singleton
    fun bindOkHttpClient(okHttpClient: OkHttpClientImpl): OkHttpClient
}

interface RetrofitProvider {
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit
    fun provideOkHttpClient(): OkHttpClient
}


class RetrofitProviderImpl @Inject constructor() : RetrofitProvider {

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    override fun provideOkHttpClient(): OkHttpClient {
        return okHttpClient
    }
}


class OkHttpClientImpl @Inject constructor() : OkHttpClient() {
    // Create a custom client
    init {
        OkHttpClient.Builder()
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
    }
}

