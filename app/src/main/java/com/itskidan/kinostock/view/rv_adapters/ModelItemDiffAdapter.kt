package com.itskidan.myapplication

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.itskidan.kinostock.data.entity.Film

import com.itskidan.kinostock.domain.OnItemClickListener

import com.itskidan.recyclerviewlesson.model.ModelItem


class ModelItemDiffAdapter(private val itemClickListener: OnItemClickListener) :
    AsyncListDifferDelegationAdapter<ModelItem>(DIFF_CALLBACK) {
    init {
        delegatesManager
            .addDelegate(FilmDelegateAdapter(itemClickListener))

    }
    fun updateItems(newDataItems: List<ModelItem>) {
        items = newDataItems.toMutableList()
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ModelItem>() {
            override fun areItemsTheSame(oldItem: ModelItem, newItem: ModelItem): Boolean {
                if (oldItem is Film && newItem is Film) {
                    return oldItem.id == newItem.id
                }
                return false
            }

            override fun areContentsTheSame(oldItem: ModelItem, newItem: ModelItem): Boolean {
                if (oldItem is Film && newItem is Film) {
                    return oldItem.id == newItem.id &&
                            oldItem.title == newItem.title &&
                            oldItem.poster == newItem.poster &&
                            oldItem.releaseDate == newItem.releaseDate &&
                            oldItem.description == newItem.description &&
                            oldItem.rating == newItem.rating &&
                            oldItem.isInFavorites == newItem.isInFavorites
                }
                return false
            }

        }
    }
}