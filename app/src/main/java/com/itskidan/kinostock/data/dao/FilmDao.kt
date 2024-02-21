package com.itskidan.kinostock.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.itskidan.kinostock.data.entity.Film
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    // We make a query on the entire table
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): Flow<List<Film>>

    // Put the list in the database, in case of a conflict, overwrite it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list: ArrayList<Film>)

    @Delete
    fun deleteFilmFromDB(filmList: List<Film>)

    @Update
    fun updateFilmFromDB(film: Film)
}