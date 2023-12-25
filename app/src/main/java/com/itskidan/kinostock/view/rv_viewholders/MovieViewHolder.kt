package com.itskidan.kinostock.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.MovieListSampleViewBinding
import com.itskidan.kinostock.domain.Film
import com.itskidan.kinostock.utils.ApiConstants

class MovieViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val binding = MovieListSampleViewBinding.bind(item)
    fun bindSample(film: Film) = with(binding) {
        //Specify the container in which our image will “live”
        Glide.with(itemView)
            .load(ApiConstants.IMAGES_URL + "w342" + film.poster)
            .centerCrop()
            .into(imagePosterMovie)
        titleMovieText.text = film.title
        releaseYearNumber.text = film.releaseDate
        descriptionText.text = film.description
        ratingNumber.text = String.format("%.1f", film.rating)
        movieRating.setRatingAnimated((film.rating*10).toInt())

        if (film.isInFavorites) imageFavorite.setImageResource(R.drawable.ic_round_favorite_24)
        else imageFavorite.setImageResource(R.drawable.ic_favorite_border_24)

    }
}