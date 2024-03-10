package com.itskidan.kinostock.utils

import com.itskidan.kinostock.data.entity.FavoritesFilm
import com.itskidan.kinostock.data.entity.Film

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
}