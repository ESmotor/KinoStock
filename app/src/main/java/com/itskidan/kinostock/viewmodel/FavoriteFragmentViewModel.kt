package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoriteFragmentViewModel : ViewModel() {

    @Inject
    lateinit var repository: MainRepository

    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor

    var databaseFromDB : Observable<List<Film>>


    init {
        App.instance.dagger.inject(this)
        databaseFromDB = interactor.getFilmsFromDB()
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
            }.let { ArrayList(it) }
        }
    }

}