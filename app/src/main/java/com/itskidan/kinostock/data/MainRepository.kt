package com.itskidan.kinostock.data

import com.itskidan.kinostock.data.dao.FilmDao
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.utils.Converter.toFavoritesFilm
import com.itskidan.kinostock.utils.Converter.toFilm
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Singleton

@Singleton
class MainRepository(private val filmDao: FilmDao) {

    fun putToDB(filmsList: ArrayList<Film>) {
        // Queries to the database must be in a separate thread
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertFilmsListToCacheDB(filmsList)
        }
    }

    fun getFilmsFromDB(): Observable<List<Film>> {
        return filmDao.getCachedFilms()
    }

    fun getFavoritesFilmsFromDB(): Observable<List<Film>> {
        val result = filmDao.getCachedFavoritesFilms().map { favoritesList ->
            favoritesList.map { it.toFilm() }
        }
        return result
    }

    fun clearDB() {
        Completable.fromSingle<List<Film>> {
            filmDao.clearCachedFilms()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun setAsFavoriteFilm(film: Film) {
        Completable.fromSingle<List<Film>> {
            filmDao.insertFilmToFavoriteDB(film.toFavoritesFilm())
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun removeFromFavoriteFilm(film: Film) {
        Completable.fromSingle<List<Film>> {
            filmDao.deleteFilmByTitleFromFavoriteDB(film.title)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}