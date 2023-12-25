package com.itskidan.kinostock.view.rv_adapters

import androidx.recyclerview.widget.DiffUtil
import com.itskidan.kinostock.domain.Film

class DiffMovieAdapter(
    val oldList: ArrayList<Film>,
    val newList: ArrayList<Film>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie = oldList[oldItemPosition]
        val newMovie = newList[newItemPosition]
        return oldMovie.poster == newMovie.poster &&
                oldMovie.title == newMovie.title &&
                oldMovie.releaseDate == newMovie.releaseDate &&
                oldMovie.description == newMovie.description &&
                oldMovie.rating == newMovie.rating &&
                oldMovie.isInFavorites == newMovie.isInFavorites
    }
}