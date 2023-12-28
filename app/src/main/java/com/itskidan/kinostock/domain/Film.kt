package com.itskidan.kinostock.domain

import android.os.Parcelable
import com.itskidan.recyclerviewlesson.model.ModelItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val description: String,
    val rating: Double = 0.0,
    var isInFavorites: Boolean
) : Parcelable, ModelItem