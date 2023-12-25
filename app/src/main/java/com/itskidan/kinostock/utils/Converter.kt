package com.itskidan.kinostock.utils

import com.itskidan.kinostock.data.TmdbFilm
import com.itskidan.kinostock.domain.Film

object Converter {
    fun convertApiListToDtoList(list: List<TmdbFilm>?): ArrayList<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    id = it.id,
                    title = it.title,
                    poster = it.posterPath,
                    releaseDate = it.releaseDate,
                    description = it.overview,
                    rating = it.voteAverage,
                    isInFavorites = false
                )
            )
        }
        return ArrayList(result)
    }
}