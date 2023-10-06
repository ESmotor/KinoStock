package com.itskidan.kinostock.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.module.Poster

open class DataModel : ViewModel() {
    val mainToDetailFragMovie: MutableLiveData<Movie> by lazy {
        MutableLiveData<Movie>()
    }
    val mainToDetailFragPosition: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val actualMovieList: MutableLiveData<ArrayList<Movie>> by lazy {
        MutableLiveData<ArrayList<Movie>>()
    }
    val actualPosterList: MutableLiveData<ArrayList<Poster>> by lazy {
        MutableLiveData<ArrayList<Poster>>()
    }
}