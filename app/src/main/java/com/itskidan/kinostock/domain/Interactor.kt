package com.itskidan.kinostock.domain

import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.TmdbResultsDto
import com.itskidan.kinostock.utils.API
import com.itskidan.kinostock.utils.Converter
import com.itskidan.kinostock.viewmodel.MainFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Interactor @Inject constructor(private val repository: MainRepository, private val retrofitService: TmdbApi) {

    // We will pass a callback from the view model to the constructor to react to when the movies
    // are received
    // and the page to load (this is for pagination)
    fun getFilmsFromApi(page: Int, callback: MainFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(apiKey = API.KEY, language = "en-US", page = page)
            .enqueue(object : Callback<TmdbResultsDto> {
                override fun onResponse(
                    call: Call<TmdbResultsDto>,
                    response: Response<TmdbResultsDto>
                ) {
                    // If successful, we call the method, pass onSuccess and a list of movies to this callback
                    callback.onSuccess(
                        films =Converter.convertApiListToDtoList(response.body()?.tmdbFilms),
                        page = response.body()!!.page,
                        totalPages = response.body()!!.totalPages
                    )
                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    // In case of failure, another callback method is called
                    callback.onFailure()
                }

            })
    }
    fun getFilmsDB(): ArrayList<Film> = repository.filmsDataBase
}