package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.Movie

class FavoriteFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<ArrayList<Movie>>()

    // Initializing the interactor
    var interactor: Interactor = App.instance.interactor

    init {
        val films = interactor.getFilmsDB()
        filmsListLiveData.postValue(films)
    }

    fun makeFavoriteFilmsDataBase(filmsDataBase: ArrayList<Movie>): ArrayList<Movie> {
        return ArrayList(filmsDataBase.filter { movie -> movie.isFavorite })
    }

    fun handleSearch(newText: String?, favoriteFilmsDataBase: ArrayList<Movie>): ArrayList<Movie> {
        return if (newText.isNullOrEmpty()) {
            favoriteFilmsDataBase
        } else {
            favoriteFilmsDataBase.filter {
                it.title.contains(
                    newText,
                    true
                )
            }.let { ArrayList<Movie>(it) }
        }
    }
}