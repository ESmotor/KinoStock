package com.itskidan.kinostock.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.MovieListSampleViewBinding
import com.itskidan.kinostock.module.Movie

class MovieAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    var data = ArrayList<Movie>()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_sample_view, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindSample(data[position])
        holder.binding.cardViewMainBg.setOnClickListener {
            clickListener.click(data[position],position)
        }
    }

    //Interface for processing clicks

    interface OnItemClickListener {
        fun click(movie: Movie,position: Int)
    }

}