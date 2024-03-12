package com.itskidan.core_impl

import android.content.Context
import androidx.room.Room
import com.itskidan.core_api.dao.FilmDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {
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