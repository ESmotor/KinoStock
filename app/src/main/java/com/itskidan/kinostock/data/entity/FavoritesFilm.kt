package com.itskidan.kinostock.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.itskidan.recyclerviewlesson.model.ModelItem
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "favorites_cached_films",
    indices = [Index(value = ["title"], unique = true)]
)
data class FavoritesFilm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("poster_path") var poster: String?,
    @ColumnInfo("overview") val description: String,
    @ColumnInfo("vote_average") val rating: Double = 0.0,
    @ColumnInfo("date_of_release") val releaseDate: String,
    @ColumnInfo("is_favorite") var isInFavorites: Boolean
) : Parcelable, ModelItem {
    init {
        // null checking
        if (poster == null) {
            // put standard
            poster = ""
        }
    }
    override fun toString(): String {
        return "Id:$id, Title:$title, Favorite: $isInFavorites"
    }
}