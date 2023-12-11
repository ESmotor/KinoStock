package com.itskidan.kinostock.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.MovieListSampleViewBinding
import com.itskidan.kinostock.domain.Movie

class MovieViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val binding = MovieListSampleViewBinding.bind(item)
    fun bindSample(movie: Movie) = with(binding) {
        //Specify the container in which our image will “live”
        Glide.with(itemView)
            .load(movie.imagePoster)
            .centerCrop()
            .into(imagePosterMovie)
        titleMovieText.text = movie.title
        releaseYearNumber.text = movie.releaseYear.toString()
        descriptionText.text = movie.description
        ratingNumber.text = (movie.rating.toDouble()/10.0).toString()
        movieRating.setRatingAnimated(movie.rating)

        if (movie.isFavorite) imageFavorite.setImageResource(R.drawable.ic_round_favorite_24)
        else imageFavorite.setImageResource(R.drawable.ic_favorite_border_24)

    }
}