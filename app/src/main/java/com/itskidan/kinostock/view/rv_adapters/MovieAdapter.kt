package com.itskidan.kinostock.view.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.R
import com.itskidan.kinostock.domain.Movie
import com.itskidan.kinostock.view.rv_viewholders.MovieViewHolder

class MovieAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<MovieViewHolder>() {
    var data = ArrayList<Movie>()

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
        fun click(movie: Movie, position: Int)
    }

}