package com.itskidan.kinostock.viewmodel

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.Film

class FavoriteFragmentViewModel : ViewModel() {

    val filmsListLiveData = MutableLiveData<ArrayList<Film>>()

    fun makeFavoriteFilmsDataBase(filmsDataBase: ArrayList<Film>): ArrayList<Film> {
        return ArrayList(filmsDataBase.filter { movie -> movie.isInFavorites })
    }

    fun handleSearch(newText: String?, favoriteFilmsDataBase: ArrayList<Film>): ArrayList<Film> {
        return if (newText.isNullOrEmpty()) {
            favoriteFilmsDataBase
        } else {
            favoriteFilmsDataBase.filter {
                it.title.contains(
                    newText,
                    true
                )
            }.let { ArrayList<Film>(it) }
        }
    }

    fun putNewData(list: ArrayList<Film>){
        filmsListLiveData.postValue(list)
    }
}