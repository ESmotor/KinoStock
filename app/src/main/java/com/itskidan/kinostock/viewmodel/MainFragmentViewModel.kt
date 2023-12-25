package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.Film
import timber.log.Timber

class MainFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<ArrayList<Film>>()
    var isAllPagesLoaded = false
    var isLoading = false
    var totalPages = 1
    var currentPage = 1

    // Initializing the interactor
    var interactor: Interactor = App.instance.interactor

    init {
        interactor.getFilmsFromApi(page = currentPage, callback = object : ApiCallback {
            override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                filmsListLiveData.postValue(films)
                this@MainFragmentViewModel.totalPages = totalPages
                Timber.tag("MyLog").d("Success")
            }

            override fun onFailure() {
                Timber.tag("MyLog").d("Failure")
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
        isLoading = true
        interactor.getFilmsFromApi(page = currentPage, callback = object : ApiCallback {
            override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                val newData = filmsListLiveData.value
                newData?.addAll(films)
                filmsListLiveData.postValue(newData!!)
                Timber.tag("MyLog").d("Success")
                isLoading = false
            }

            override fun onFailure() {
                Timber.tag("MyLog").d("Failure")
                currentPage--
                isLoading = false
            }
        })
        isAllPagesLoaded = currentPage==totalPages
    }

    interface ApiCallback {
        fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int)
        fun onFailure()
    }

}