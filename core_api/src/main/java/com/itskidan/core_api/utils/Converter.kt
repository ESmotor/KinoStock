package com.itskidan.core_api.utils

import com.itskidan.core_api.entity.FavoritesFilm
import com.itskidan.core_api.entity.Film


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