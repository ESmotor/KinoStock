package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.ViewModel
import com.itskidan.core_api.entity.Film
import com.itskidan.core_impl.MainRepository
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoriteFragmentViewModel : ViewModel() {

    @Inject
    lateinit var repository: MainRepository

    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor

    var databaseFromFavoriteDB : Observable<List<Film>>


    init {
        App.instance.dagger.inject(this)
        databaseFromFavoriteDB = interactor.getFavoritesFilmsFromDB()
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