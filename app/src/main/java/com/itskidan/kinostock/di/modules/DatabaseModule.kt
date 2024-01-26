package com.itskidan.kinostock.di.modules

import android.app.Application
import android.content.Context
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.database.DatabaseHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return App.instance
    }
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }
    @Singleton
    @Provides
    fun provideDatabaseHelper(context: Context) = DatabaseHelper(context)

    @Provides
    @Singleton
    fun provideRepository(databaseHelper: DatabaseHelper) = MainRepository(databaseHelper)
}