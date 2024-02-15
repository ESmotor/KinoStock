package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.data.entity.Film

class FavoriteFragmentViewModel : ViewModel() {

    val filmsListLiveData = MutableLiveData<ArrayList<Film>>()

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

}