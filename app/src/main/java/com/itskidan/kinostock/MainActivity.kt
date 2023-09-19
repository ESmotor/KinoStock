package com.itskidan.kinostock

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TopAppBar Settings
        topAppBarClickListener()

        //Bottom Navigation Menu Settings
        bottomAppBarClickListener()

        //Movie List Recycler View
        //create main Movie Adapter with click listener on items
        val movieAdapter = MovieListAdapter(object : MovieListAdapter.OnItemClickListener {
            override fun click(movie: Movie, position: Int) {
            }
        })
        // create LayoutManager
        val layoutManagerMovie =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        // create ItemDecoration for offset
        val movieItemsDecoration = MovieItemsDecoration(8)

        //setup Movie Adapter to our Recycler view
        binding.rvMoovieList.apply {
            this?.adapter = movieAdapter
            this?.layoutManager = layoutManagerMovie
            this?.addItemDecoration(movieItemsDecoration)
        }
        // add some data to Recycler view
        val dataMovie = movieData()
        movieAdapter.addAllMovies(dataMovie)

        // Top Posters Recycler View
        //create Poster Adapter
        val posterAdapter = PosterAdapter()
        // create LayoutManager
        val layoutManagerPoster =
            LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        //setup Movie Adapter to our Recycler view
        binding.rvPoster.apply {
            this?.adapter = posterAdapter
            this?.layoutManager = layoutManagerPoster
        }
        // add some data to Recycler view
        val dataPoster = posterData()
        posterAdapter.addAllPoster(dataPoster)

    }


    private fun bottomAppBarClickListener() {
        binding.bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    showToastMsg("Favorites")
                    true
                }

                R.id.watch_later -> {
                    showToastMsg("Watch Later")
                    true
                }

                R.id.collection -> {
                    showToastMsg("Film collection")
                    true
                }

                else -> false
            }
        }
    }

    private fun topAppBarClickListener() {
        with(binding) {
            topAppBar?.setNavigationOnClickListener {
                showToastMsg("Navigation menu")
            }
            topAppBar?.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        showToastMsg("Settings")
                        true
                    }

                    else -> false
                }
            }

        }
    }

    // Support Functions for init data and Toast
    private fun showToastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun movieData(): ArrayList<Movie> {
        val resultList = ArrayList<Movie>()
        repeat(20) {
            val index = it % imageIdList.size
            val movie =
                Movie(
                    (1..10).random() * it,
                    imageIdList[index],
                    titleList[index],
                    2023,
                    "It is a long established fact that a reader will be distracted " +
                            "by the readable content of a page when looking at its layout. " +
                            "The point of using Lorem Ipsum is that it has a more-or-less " +
                            "normal distribution of letters, as opposed to using.",
                    6.5
                )
            resultList.add(movie)
        }
        return resultList
    }

    private fun posterData(): ArrayList<MoviePoster> {
        val resultList = ArrayList<MoviePoster>()
        repeat(10) {
            val index = it % imageIdList.size
            val poster = MoviePoster(
                (1000..9999).random() * it,
                imageIdList[index],
                titleList[index],
                subtitleList[index]
            )
            resultList.add(poster)
        }
        return resultList
    }


    companion object {
        private val imageIdList = listOf(
            R.drawable.movie_poster1,
            R.drawable.movie_poster2,
            R.drawable.movie_poster3,
            R.drawable.movie_poster4
        )
        private val titleList = listOf(
            "Outbreak",
            "Header",
            "Astronaut",
            "Wizard OZ"
        )
        private val subtitleList = listOf(
            "Action",
            "Horror",
            "Fantastic",
            "Cartoon"
        )
    }
}