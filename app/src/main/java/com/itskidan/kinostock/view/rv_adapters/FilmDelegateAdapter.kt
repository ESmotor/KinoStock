package com.itskidan.kinostock.view.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.itskidan.core_api.domain.ModelItem
import com.itskidan.core_api.entity.Film
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.MovieListSampleViewBinding
import com.itskidan.kinostock.domain.OnItemClickListener
import com.itskidan.kinostock.utils.ApiConstants

class FilmDelegateAdapter(private val itemClickListener: OnItemClickListener) :
    AbsListItemAdapterDelegate<Film, ModelItem, FilmDelegateAdapter.FilmViewHolder>() {

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = MovieListSampleViewBinding.bind(itemView)

        fun onBind(film: Film) = with(binding) {
            Glide.with(itemView)
                .load(ApiConstants.IMAGES_URL + "w342" + film.poster)
                .centerCrop()
                .into(imagePosterMovie)
            titleMovieText.text = film.title
            releaseYearNumber.text = film.releaseDate
            descriptionText.text = film.description
            ratingNumber.text = String.format("%.1f", film.rating)
            movieRating.setRatingAnimated((film.rating * 10).toInt())
            if (film.isInFavorites) imageFavorite.setImageResource(R.drawable.ic_round_favorite_24)
            else imageFavorite.setImageResource(R.drawable.ic_favorite_border_24)
        }

    }

    override fun isForViewType(
        item: ModelItem,
        items: MutableList<ModelItem>,
        position: Int
    ): Boolean {
        return item is Film
    }

    override fun onCreateViewHolder(parent: ViewGroup): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_sample_view, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(
        item: Film,
        holder: FilmViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.onBind(item)
        holder.binding.cardViewMainBg.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
    }

}