package com.itskidan.kinostock.domain

import com.itskidan.kinostock.data.TmdbResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/popular")
    fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>

    @GET("3/search/movie")
    fun searchFilms(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>

}