package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.SingleLiveEvent
import com.itskidan.kinostock.utils.Constants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {

    val progressBarSubject = BehaviorSubject.create<Boolean>()
    var isAllPagesLoaded = false
    var totalPages = 1
    var currentPage = 1
    val connectionProblemEvent = SingleLiveEvent<String>()
    var searchingText = ""
    var totalPagesForSearch = 1
    var currentPageSearch = 1

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


    fun getFilms(text: String = "", isNextPage: Boolean) {
        if (isNextPage) {
            if (searchingText != "") {
                searchFilms(searchingText)
            } else {
                getPopularFilms()
            }

        } else {
            resetPagination()
            if (text != "") {
                searchingText = text
                searchFilms(text)
            } else {
                searchingText = ""
                getPopularFilms()
            }
        }
    }

    private fun resetPagination() {
        totalPages = 1
        currentPage = 1
        totalPagesForSearch = 1
        currentPageSearch = 1
        repository.clearDB()
    }

    private fun getPopularFilms() {

        isShowProgressBar(true)
        interactor.getFilmsFromApi(
            page = currentPage,
            callback = object : ApiCallback {
                override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                    Timber.tag("MyLog").d("Loading from the API")
                    Timber.tag("MyLog")
                        .d("Current page come = $currentPage, from totalPage = $totalPages")
                    saveUpdateTimeDatabase()
                    this@MainFragmentViewModel.totalPages = totalPages
                    currentPage++
                    isAllPagesLoaded = currentPage == totalPages
                    isShowProgressBar(false)
                }

                override fun onFailure() {
                    Timber.tag("MyLog").d("Failure init data, loading from the database")
                    postConnectionProblemEvent()
                    isShowProgressBar(false)

                }
            })
    }


    private fun searchFilms(text: String) {

        isShowProgressBar(true)
        interactor.searchFilmsFromApi(
            query = text,
            page = currentPageSearch,
            callback = object : ApiCallback {
                override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                    Timber.tag("MyLog").d("Searching from the API")
                    Timber.tag("MyLog")
                        .d("Current page come = $page, from totalPage = $totalPages")
                    saveUpdateTimeDatabase()
                    this@MainFragmentViewModel.totalPagesForSearch = totalPages
                    currentPageSearch++
                    isShowProgressBar(false)
                }

                override fun onFailure() {
                    Timber.tag("MyLog").d("Failure init data, loading from the database")
                    postConnectionProblemEvent()
                    isShowProgressBar(false)

                }
            })
    }


    private fun isShowProgressBar(element: Boolean) {
        progressBarSubject.onNext(element)
    }

    fun isDatabaseUpdateTime(min: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = App.instance.sharedPreferences.getLong(
            Constants.LAST_UPDATE_TIME_DATABASE_KEY,
            0L
        )
        return currentTime - lastUpdateTime > TimeUnit.MINUTES.toMillis(min)
    }

    interface ApiCallback {
        fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int)
        fun onFailure()
    }

    private fun saveUpdateTimeDatabase() {
        App.instance.sharedPreferences
            .edit()
            .putLong(
                Constants.LAST_UPDATE_TIME_DATABASE_KEY,
                System.currentTimeMillis()
            )
            .apply()
    }

    fun postConnectionProblemEvent() {
        connectionProblemEvent.postValue(Constants.SERVER_CONNECTION_PROBLEM)
    }
}