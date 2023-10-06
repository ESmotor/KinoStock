package com.itskidan.kinostock.adapters

import androidx.recyclerview.widget.DiffUtil
import com.itskidan.kinostock.module.Movie

class DiffMovieAdapter(
    val oldList: ArrayList<Movie>,
    val newList: ArrayList<Movie>
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
        return oldMovie.imagePoster == newMovie.imagePoster &&
                oldMovie.title == newMovie.title &&
                oldMovie.releaseYear == newMovie.releaseYear &&
                oldMovie.description == newMovie.description &&
                oldMovie.rating == newMovie.rating &&
                oldMovie.isFavorite == newMovie.isFavorite
    }
}