package com.itskidan.core_impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itskidan.core_api.DatabaseContract
import com.itskidan.core_api.dao.FilmDao
import com.itskidan.core_api.entity.FavoritesFilm
import com.itskidan.core_api.entity.Film


@Database(entities = [Film::class, FavoritesFilm::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase(), DatabaseContract {
    abstract override fun filmDao(): FilmDao
}