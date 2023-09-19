package com.itskidan.kinostock

data class Movie(
    val id: Int,
    val imagePoster: Int,
    val title: String,
    val releaseYear: Int,
    val description: String,
    val rating: Double
) {
}