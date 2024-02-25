package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.domain.Interactor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class FavoriteFragmentViewModel : ViewModel() {
    var sendersData = MutableSharedFlow<List<Film>>()

    @Inject
    lateinit var repository: MainRepository

    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor


    init {
        App.instance.dagger.inject(this)

        CoroutineScope(EmptyCoroutineContext).launch {
            val databaseFromDB = interactor.getFilmsFromDB()
            databaseFromDB.collect {
                sendersData.emit(it)
            }
        }
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