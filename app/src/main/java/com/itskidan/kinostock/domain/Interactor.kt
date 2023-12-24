package com.itskidan.kinostock.domain

import com.itskidan.kinostock.data.MainRepository

class Interactor(val repository: MainRepository) {
    fun getFilmsDB(): ArrayList<Movie> = repository.filmsDataBase
}