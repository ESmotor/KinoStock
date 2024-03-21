package com.itskidan.kinostock.domain

import com.itskidan.core_api.entity.Film
import com.itskidan.core_impl.MainRepository
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.utils.Constants
import com.itskidan.kinostock.utils.Converter
import com.itskidan.remote_module.TmdbApi
import com.itskidan.remote_module.entity.API
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Interactor @Inject constructor(
    private val repository: MainRepository,
    private val retrofitService: TmdbApi
) {
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val connectionProblemEvent = SingleLiveEvent<String>()

    // We will pass a callback from the view model to the constructor to react to when the movies
    // are received
    // and the page to load (this is for pagination)
    fun getFilmsFromApi(page: Int, autoDisposable: AutoDisposable) {
        retrofitService.getFilms(
            apiKey = API.KEY,
            language = "en-US",
            page = page
        )
            .subscribeOn(Schedulers.io())
            .map {
                Converter.convertApiListToDTOList(it.tmdbFilms)
            }
            .subscribeBy(
                onError = {
                    progressBarState.onNext(false)
                    postConnectionProblemEvent()
                },
                onNext = {
                    progressBarState.onNext(false)
                    saveUpdateTimeDatabase()
                    repository.putToDB(it)
                }
            ).addTo(autoDisposable)
    }

    fun searchFilmsFromApi(query: String, page: Int, autoDisposable: AutoDisposable) {
        retrofitService.searchFilms(
            apiKey = API.KEY,
            query = query,
            includeAdult = false,
            language = "en-US",
            page = page
        )
            .subscribeOn(Schedulers.io())
            .map {
                Converter.convertApiListToDTOList(it.tmdbFilms)
            }
            .subscribeBy(
                onError = {
                    progressBarState.onNext(false)
                    postConnectionProblemEvent()
                },
                onNext = {
                    progressBarState.onNext(false)
                    saveUpdateTimeDatabase()
                    repository.putToDB(it)
                }
            ).addTo(autoDisposable)
    }

    fun getFilmsFromDB(): Observable<List<Film>> = repository.getFilmsFromDB()
    fun getFavoritesFilmsFromDB(): Observable<List<Film>> = repository.getFavoritesFilmsFromDB()

    private fun saveUpdateTimeDatabase() {
        App.instance.sharedPreferences
            .edit()
            .putLong(
                Constants.LAST_UPDATE_TIME_DATABASE_KEY,
                System.currentTimeMillis()
            )
            .apply()
    }

    private fun postConnectionProblemEvent() {
        connectionProblemEvent.postValue(Constants.SERVER_CONNECTION_PROBLEM)
    }

}