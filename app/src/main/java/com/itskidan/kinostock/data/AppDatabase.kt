package com.itskidan.kinostock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itskidan.kinostock.data.dao.FilmDao
import com.itskidan.kinostock.data.entity.FavoritesFilm
import com.itskidan.kinostock.data.entity.Film

@Database(entities = [Film::class,FavoritesFilm::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}