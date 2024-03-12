package com.itskidan.kinostock.utils

import com.itskidan.kinostock.data.entity.FavoritesFilm
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.remote_module.entity.TmdbFilm

object Converter {
    fun Film.toFavoritesFilm(): FavoritesFilm {
        return FavoritesFilm(
            id = id,
            title = title,
            poster = poster,
            description = description,
            rating = rating,
            releaseDate = releaseDate,
            isInFavorites = isInFavorites
        )
    }

    fun FavoritesFilm.toFilm(): Film {
        return Film(
            id = id,
            title = title,
            poster = poster,
            description = description,
            rating = rating,
            releaseDate = releaseDate,
            isInFavorites = isInFavorites
        )
    }

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