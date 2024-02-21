package com.itskidan.kinostock.data

import com.itskidan.kinostock.data.dao.FilmDao
import com.itskidan.kinostock.data.entity.Film
import kotlinx.coroutines.flow.Flow
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

    fun getAllFromDB(): Flow<List<Film>> {
        return filmDao.getCachedFilms()
    }

    fun clearDB(dataBase: ArrayList<Film>) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteFilmFromDB(dataBase)
        }
    }

    fun setAsFavoriteFilm(film: Film) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.updateFilmFromDB(film)
        }
    }

    fun removeFromFavoriteFilm(film: Film) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.updateFilmFromDB(film)
        }
    }


}