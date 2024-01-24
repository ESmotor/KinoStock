package com.itskidan.kinostock.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.Film
import timber.log.Timber
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<ArrayList<Film>>()
    var isAllPagesLoaded = false
    var totalPages = 1
    var currentPage = 1

    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        interactor.getFilmsFromApi(page = 1, callback = object : ApiCallback {
            override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                val newDataFilms = if (filmsListLiveData.value == null) {
                    ArrayList<Film>()
                } else {
                    filmsListLiveData.value
                }
                newDataFilms?.addAll(films)
                Timber.tag("MyLog").d("newDataFilmsSize = ${newDataFilms?.size}")
                filmsListLiveData.postValue(newDataFilms ?: ArrayList())
                this@MainFragmentViewModel.totalPages = totalPages
                Timber.tag("MyLog")
                    .d("Success init data, ${films.size},filmsListLiveDataSize = ${filmsListLiveData.value?.size}, total pages = $totalPages")
            }

            override fun onFailure() {
                Timber.tag("MyLog").d("Failure init data")
            }

        })
    }

    fun handleSearch(newText: String?): ArrayList<Film>? {
        val filmsDataBase = filmsListLiveData.value
        return if (newText.isNullOrEmpty()) {
            filmsDataBase
        } else {
            filmsDataBase?.filter {
                it.title.contains(
                    newText,
                    true
                )
            }?.let { ArrayList<Film>(it) }
        }
    }

    fun fetchFilms() {
        currentPage++
        interactor.getFilmsFromApi(page = currentPage, callback = object : ApiCallback {
            override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                val newDataFilms = filmsListLiveData.value
                newDataFilms?.addAll(films)
                filmsListLiveData.postValue(newDataFilms ?: ArrayList())
                Timber.tag("MyLog")
                    .d("Success Load Data, ${films.size}, filmsListLiveDataSize = ${filmsListLiveData.value?.size} , page = $page")
            }

            override fun onFailure() {
                Timber.tag("MyLog").d("Failure: Load films from DataBase")
                filmsListLiveData.postValue(interactor.getFilmsFromDB())
                currentPage--
            }
        })
        isAllPagesLoaded = currentPage == totalPages
    }

    interface ApiCallback {
        fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int)
        fun onFailure()
    }

}