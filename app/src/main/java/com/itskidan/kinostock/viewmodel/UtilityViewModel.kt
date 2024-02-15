package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.data.entity.Film

open class UtilityViewModel : ViewModel() {
    val chosenFilm: MutableLiveData<Film> by lazy {
        MutableLiveData<Film>()
    }
    val chosenMoviePosition: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val actualFilmList: MutableLiveData<ArrayList<Film>> by lazy {
        MutableLiveData<ArrayList<Film>>()
    }
    val favoriteFilmList: MutableLiveData<ArrayList<Film>> by lazy {
        MutableLiveData<ArrayList<Film>>()
    }
}