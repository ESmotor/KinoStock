package com.itskidan.kinostock.data

import com.itskidan.kinostock.data.dao.FilmDao
import com.itskidan.kinostock.data.entity.Film
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
            filmDao.insertAll(filmsList)
        }
    }

    fun getAllFromDB(): Observable<List<Film>> {
        return filmDao.getCachedFilms()
    }

    fun clearDB(dataBase: ArrayList<Film>) {
        Completable.fromSingle<List<Film>> {
            filmDao.deleteFilmFromDB(dataBase)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun setAsFavoriteFilm(film: Film) {
        Completable.fromSingle<List<Film>> {
            filmDao.updateFilmFromDB(film)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun removeFromFavoriteFilm(film: Film) {
        Completable.fromSingle<List<Film>> {
            filmDao.updateFilmFromDB(film)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


}