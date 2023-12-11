package com.itskidan.kinostock.data

import com.itskidan.kinostock.R
import com.itskidan.kinostock.domain.Movie
import com.itskidan.kinostock.view.fragments.MainFragment

class MainRepository {
    val filmsDataBase = movieData()

    private fun movieData(): ArrayList<Movie> {
        val resultList = ArrayList<Movie>()
        val imageIdList = listOf(
            R.drawable.movie_poster1,
            R.drawable.movie_poster2,
            R.drawable.movie_poster3,
            R.drawable.movie_poster4
        )
        val titleList = listOf(
            "Outbreak",
            "Header",
            "Astronaut",
            "Wizard OZ"
        )


        repeat(9) {
            val index = it % imageIdList.size
            val movie =
                Movie(
                    (10000..99999).random() * (1 + it),
                    imageIdList[index],
                    titleList[index],
                    (1925..2023).random(),
                    "It is a long established fact that a reader will be distracted " +
                            "by the readable content of a page when looking at its layout. " +
                            "The point of using Lorem Ipsum is that it has a more-or-less " +
                            "normal distribution of letters, as opposed to using.",
                    (10..100).random(),
                    it % 3 == 0
                )
            resultList.add(movie)
        }
        return resultList
    }
}