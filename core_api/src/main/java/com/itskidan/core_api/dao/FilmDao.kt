package com.itskidan.core_api.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itskidan.core_api.entity.FavoritesFilm
import com.itskidan.core_api.entity.Film
import io.reactivex.rxjava3.core.Observable

@Dao
interface FilmDao {
    // We make a query on the entire table
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): Observable<List<Film>>
    @Query("DELETE FROM cached_films")
    fun clearCachedFilms()

    // Put the list in the database, in case of a conflict, overwrite it
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFilmsListToCacheDB(list: List<Film>)

    //methods for favorites_cached_films
    @Query("SELECT * FROM favorites_cached_films")
    fun getCachedFavoritesFilms(): Observable<List<FavoritesFilm>>

    // Put the list in the database, in case of a conflict, overwrite it
    @Query("DELETE FROM favorites_cached_films WHERE title = :title")
    fun deleteFilmByTitleFromFavoriteDB(title: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmToFavoriteDB(film: FavoritesFilm)

}