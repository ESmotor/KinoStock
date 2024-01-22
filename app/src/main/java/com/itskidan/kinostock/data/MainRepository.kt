package com.itskidan.kinostock.data

import com.itskidan.kinostock.domain.Film
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {
    val filmsDataBase = ArrayList<Film>()
}