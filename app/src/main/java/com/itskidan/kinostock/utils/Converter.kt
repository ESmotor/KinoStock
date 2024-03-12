package com.itskidan.kinostock.utils

import com.itskidan.core_api.entity.Film
import com.itskidan.remote_module.entity.TmdbFilm

object Converter {
    fun convertApiListToDTOList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    id = 0,
                    title = it.title,
                    poster = it.posterPath,
                    releaseDate = it.releaseDate,
                    description = it.overview,
                    rating = it.voteAverage,
                    isInFavorites = false
                )
            )
        }
        return result
    }
}