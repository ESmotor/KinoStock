package com.itskidan.kinostock.viewmodel

import androidx.lifecycle.ViewModel
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.SingleLiveEvent
import com.itskidan.kinostock.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class MainFragmentViewModel : ViewModel() {
    val progressBarChannel = Channel<Boolean>(Channel.CONFLATED)
    var isAllPagesLoaded = false
    var totalPages = 1
    var currentPage = 1
    val connectionProblemEvent = SingleLiveEvent<String>()
    var flowForSendersData = MutableSharedFlow<List<Film>>()

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
                flowForSendersData.emit(it)
            }
        }
    }

    // function for searching films
    fun handleSearch(filmsDataBase: ArrayList<Film>, newText: String?): ArrayList<Film> {
        return if (newText.isNullOrEmpty()) {
            filmsDataBase
        } else {
            filmsDataBase.filter {
                it.title.contains(
                    newText,
                    true
                )
            }.let { ArrayList(it) }
        }
    }


    fun getFilms() {

        isShowProgressBar(true)

        interactor.getFilmsFromApi(page = currentPage, callback = object : ApiCallback {
            override fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int) {
                Timber.tag("MyLog").d("Loading from the API")
                Timber.tag("MyLog")
                    .d("Current page come = $currentPage, from totalPage = $totalPages")
                saveUpdateTimeDatabase()
                this@MainFragmentViewModel.totalPages = totalPages
                currentPage++
                isAllPagesLoaded = currentPage == totalPages
                Timber.tag("MyLog")
                    .d("Current page out = $currentPage, from totalPage = $totalPages")
                isShowProgressBar(false)
            }

            override fun onFailure() {
                Timber.tag("MyLog").d("Failure init data, loading from the database")
                postConnectionProblemEvent()
                isShowProgressBar(false)

            }
        })

    }

    private fun isShowProgressBar(element: Boolean){
        CoroutineScope(EmptyCoroutineContext).launch {
            progressBarChannel.send(element)
        }
    }

    fun isDatabaseUpdateTime(min: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = App.instance.sharedPreferences.getLong(
            Constants.LAST_UPDATE_TIME_DATABASE_KEY,
            0L
        )
        return currentTime - lastUpdateTime > TimeUnit.MINUTES.toMillis(min)
    }

    interface ApiCallback {
        fun onSuccess(films: ArrayList<Film>, page: Int, totalPages: Int)
        fun onFailure()
    }

    private fun saveUpdateTimeDatabase() {
        App.instance.sharedPreferences
            .edit()
            .putLong(
                Constants.LAST_UPDATE_TIME_DATABASE_KEY,
                System.currentTimeMillis()
            )
            .apply()
    }

    fun postConnectionProblemEvent() {
        connectionProblemEvent.postValue(Constants.SERVER_CONNECTION_PROBLEM)
    }
}