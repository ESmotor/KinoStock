package com.itskidan.kinostock.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.PosterSampleViewBinding
import com.itskidan.kinostock.module.Poster

class PosterAdapter : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {
    val data = ArrayList<Poster>()

    class PosterViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = PosterSampleViewBinding.bind(item)

        fun bindSample(poster: Poster) = with(binding) {
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
        return data.size
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        holder.bindSample(data[position])
    }

    fun addLastPoster(post: Poster) {
        data.add(post)
        val position = data.size - 1
        notifyItemInserted(position)
    }

    fun addAllPoster(data: ArrayList<Poster>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

}

