package com.itskidan.kinostock.viewmodel

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.itskidan.kinostock.R
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.Movie
import com.itskidan.kinostock.view.fragments.MainFragment

class MainFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<ArrayList<Movie>>()

    // Initializing the interactor
    var interactor: Interactor = App.instance.interactor
    init {
        val films = interactor.getFilmsDB()
        filmsListLiveData.postValue(films)
    }

    fun handleSearch(newText: String?): ArrayList<Movie>? {
        val filmsDataBase = filmsListLiveData.value
        return if (newText.isNullOrEmpty()) {
            filmsDataBase
        } else {
            filmsDataBase?.filter {
                it.title.contains(
                    newText,
                    true
                )
            }?.let { ArrayList<Movie>(it) }
        }
    }


}