package com.itskidan.core_impl

import com.itskidan.core_api.dao.FilmDao
import com.itskidan.core_api.entity.Film
import com.itskidan.core_api.utils.Converter.toFavoritesFilm
import com.itskidan.core_api.utils.Converter.toFilm
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepository @Inject constructor(private val filmDao: FilmDao) {

    fun putToDB(filmsList: List<Film>) {
        // Queries to the database must be in a separate thread
        val favListFim : Observable<List<Film>> = getFavoritesFilmsFromDB()
        Completable.fromSingle<List<Film>> {
            filmDao.insertFilmsListToCacheDB(filmsList)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
//fun putToDB(filmsList: List<Film>) {
//    // Получаем список избранных фильмов
//    val favListFilmObservable: Observable<List<Film>> = getFavoritesFilmsFromDB()
//
//    // Преобразуем Observable<List<Film>> в Single<List<Film>> и подписываемся на него
//    val valera = favListFilmObservable
//        .flatMapSingle { favListFilm ->
//            // Преобразуем список фильмов в мапу для быстрого доступа к избранным фильмам
//            val favMap = favListFilm.associateBy { it.title }
//
//            // Устанавливаем флаг isFavorite для каждого фильма в списке filmsList
//            Single.just(filmsList.map { film ->
//                film.copy(isInFavorites = favMap.containsKey(film.title))
//            })
//        }
//        .observeOn(Schedulers.io())
//        .subscribe({ updatedFilmsList ->
//            // Передаем обновленный список фильмов в базу данных
//            filmDao.insertFilmsListToCacheDB(updatedFilmsList)
//        }, { error ->
//            // Обработка ошибок
//            println("Error: ${error.localizedMessage}")
//        })
//}



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