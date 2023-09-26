package com.itskidan.kinostock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.adapters.DiffMovieAdapter
import com.itskidan.kinostock.adapters.MovieAdapter
import com.itskidan.kinostock.adapters.MovieItemsDecoration
import com.itskidan.kinostock.adapters.PosterAdapter
import com.itskidan.kinostock.databinding.ActivityMainBinding
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.module.Poster


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Let's register our transition to a new activity and define a callback for the future, in case it comes in handy
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data?.getStringExtra("key")
                }
            }

        //TopAppBar Settings
        topAppBarClickListener()

        //Bottom Navigation Menu Settings
        bottomAppBarClickListener()

        //Movie List Recycler View
        //create main Movie Adapter with click listener on items
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnItemClickListener {
            override fun click(movie: Movie) {
                val bundle = Bundle()
                bundle.putParcelable("movie", movie)
                val intent = Intent(this@MainActivity, DetailMovieActivity::class.java)
                intent.putExtras(bundle)
                launcher?.launch(intent)
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
        //update data with DiffUtil
        fun updateDiffDataMovie(newData: ArrayList<Movie>) {
            val oldData = movieAdapter.data
            val movieDiff = DiffMovieAdapter(oldData, newData)
            val diffResult = DiffUtil.calculateDiff(movieDiff)
            movieAdapter.data = newData
            diffResult.dispatchUpdatesTo(movieAdapter)
        }
        // add some data to Recycler view
        val dataMovie = movieData()
        updateDiffDataMovie(dataMovie)

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
                    (10000..99999).random() * (1 + it),
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

    private fun posterData(): ArrayList<Poster> {
        val resultList = ArrayList<Poster>()
        repeat(10) {
            val index = it % imageIdList.size
            val poster = Poster(
                (1000..9999).random() * (it + 1),
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