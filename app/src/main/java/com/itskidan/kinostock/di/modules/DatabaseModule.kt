package com.itskidan.kinostock.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.AppDatabase
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.dao.FilmDao
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
    fun provideFilmDao(context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "film_db"
        ).build().filmDao()


    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao) = MainRepository(filmDao)
}