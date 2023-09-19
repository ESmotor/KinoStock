package com.itskidan.kinostock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.databinding.PosterSampleViewBinding

class PosterAdapter : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {
    private val posterList = ArrayList<MoviePoster>()

    class PosterViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = PosterSampleViewBinding.bind(item)

        fun bindSample(poster: MoviePoster) = with(binding) {
            posterImageView.setImageResource(poster.imageId)
            posterTitle.text = poster.title
            posterSubTitle.text = poster.subTilte
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.poster_sample_view, parent, false)
        return PosterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posterList.size
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        holder.bindSample(posterList[position])
    }

    fun addLastPoster(post: MoviePoster) {
        posterList.add(post)
        val position = posterList.size - 1
        notifyItemInserted(position)
    }

    fun addAllPoster(data: ArrayList<MoviePoster>) {
        posterList.clear()
        posterList.addAll(data)
        notifyDataSetChanged()
    }

}

