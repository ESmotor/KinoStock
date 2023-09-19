package com.itskidan.kinostock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.databinding.MovieListSampleViewBinding

class MovieListAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    private val movieList = ArrayList<Movie>()

    class MovieViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = MovieListSampleViewBinding.bind(item)
        fun bindSample(movie: Movie) = with(binding) {
            imagePosterMovie.setImageResource(movie.imagePoster)
            titleMovieText.text = movie.title
            releaseYearNumber.text = movie.releaseYear.toString()
            descriptionText.text = movie.description
            ratingNumber.text = movie.rating.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_sample_view, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindSample(movieList[position])
    }

    fun addLastMovie(movie: Movie) {
        movieList.add(movie)
        val position = movieList.size - 1
        notifyItemInserted(position)
    }
    fun addAllMovies(data: ArrayList<Movie>) {
        movieList.clear()
        movieList.addAll(data)
        notifyDataSetChanged()
    }



    //Interface for processing clicks

    interface OnItemClickListener {
        fun click(movie: Movie, position: Int)
    }

}