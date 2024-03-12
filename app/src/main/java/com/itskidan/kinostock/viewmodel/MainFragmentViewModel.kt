package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.domain.AutoDisposable
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.utils.Constants
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {

    private var currentPage = 1
    private var searchingText = ""
    private var currentPageSearch = 1

    var databaseFromDB: Observable<List<Film>>
    var databaseFromFavoriteDB: Observable<List<Film>>

    @Inject
    lateinit var repository: MainRepository

    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor


    init {
        App.instance.dagger.inject(this)
        databaseFromDB = interactor.getFilmsFromDB()
        databaseFromFavoriteDB = interactor.getFavoritesFilmsFromDB()
    }


    fun getFilms(text: String = "", isNextPage: Boolean, autoDisposable: AutoDisposable) {
        if (isNextPage) {
            if (searchingText != "") {
                searchFilms(searchingText, autoDisposable)
            } else {
                getPopularFilms(autoDisposable)
            }

        } else {
            resetPagination()
            if (text != "") {
                searchingText = text
                searchFilms(text, autoDisposable)
            } else {
                searchingText = ""
                getPopularFilms(autoDisposable)
            }
        }
    }

    private fun resetPagination() {
        currentPage = 1
        currentPageSearch = 1
        repository.clearDB()
    }

    private fun getPopularFilms(autoDisposable: AutoDisposable) {
        interactor.getFilmsFromApi(page = currentPage++, autoDisposable = autoDisposable)
    }


    private fun searchFilms(text: String, autoDisposable: AutoDisposable) {
        interactor.searchFilmsFromApi(
            query = text,
            page = currentPageSearch++,
            autoDisposable = autoDisposable
        )
    }

    fun isDatabaseUpdateTime(min: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = App.instance.sharedPreferences.getLong(
            Constants.LAST_UPDATE_TIME_DATABASE_KEY,
            0L
        )
        return currentTime - lastUpdateTime > TimeUnit.MINUTES.toMillis(min)
    }


}