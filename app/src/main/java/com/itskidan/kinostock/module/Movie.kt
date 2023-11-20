package com.itskidan.kinostock.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val imagePoster: Int,
    val title: String,
    val releaseYear: Int,
    val description: String,
    val rating: Int,
    var isFavorite: Boolean
) : Parcelable