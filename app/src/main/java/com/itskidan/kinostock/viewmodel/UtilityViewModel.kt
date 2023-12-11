package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.domain.Movie

open class UtilityViewModel : ViewModel() {
    val chosenMovie: MutableLiveData<Movie> by lazy {
        MutableLiveData<Movie>()
    }
    val chosenMoviePosition: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val actualMovieList: MutableLiveData<ArrayList<Movie>> by lazy {
        MutableLiveData<ArrayList<Movie>>()
    }
}