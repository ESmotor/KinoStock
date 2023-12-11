package com.itskidan.kinostock.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.Movie

class DetailFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<ArrayList<Movie>>()

    // Initializing the interactor
    var interactor: Interactor = App.instance.interactor

    init {
        val films = interactor.getFilmsDB()
        filmsListLiveData.postValue(films)
    }

    fun onShareClick(context: Context, movie: Movie) {
        //Create an intent
        val intent = Intent()
        //Specify the action with which it is launched
        intent.action = Intent.ACTION_SEND
        //Put data about our movie
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Check out this film: ${movie.title} \n\n ${movie.description}"
        )
        //Specify the MIME type so that the system knows which application to offer
        intent.type = "text/plain"
        //Launch our activity
        context.startActivity(Intent.createChooser(intent, "Share To:"))
    }
}