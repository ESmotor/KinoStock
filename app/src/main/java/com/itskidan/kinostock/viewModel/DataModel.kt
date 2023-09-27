package com.itskidan.kinostock.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.module.Movie

open class DataModel : ViewModel() {
    val dataMainFragToDetailFrag: MutableLiveData<Movie> by lazy {
        MutableLiveData<Movie>()
    }
}