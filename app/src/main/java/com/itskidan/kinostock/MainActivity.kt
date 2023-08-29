package com.itskidan.kinostock

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itskidan.kinostock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    private val newPosterAdapter = PosterAdapter()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.topAppBar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    showToastMsg("Settings")
                    true
                }

                else -> false
            }
        }
        init()
        onClickTopToolBar()
        onClickBottomMenu()
    }

    private fun showToastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onClickBottomMenu() {
        mainBinding.bottomNavigation?.setOnItemSelectedListener {
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

    private fun onClickTopToolBar() {
        with(mainBinding) {
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

    private fun init() {
        mainBinding.apply {
            rvPoster?.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            rvPoster?.adapter = newPosterAdapter
            repeat(10) {
                val index = it % imageIdList.size

                val poster =
                    MoviePoster(imageIdList[index], titleList[index], subtitleList[index])
                newPosterAdapter.addPost(poster)
            }
        }
    }

}