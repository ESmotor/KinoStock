package com.itskidan.kinostock

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.databinding.ActivityMainBinding
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.viewModel.DataModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var currentMoviePos: Int? = null
    var currentMovie: Movie? = null
    lateinit var currentMovieList: ArrayList<Movie>
    private val dataModel: DataModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataModel.mainToDetailFragPosition.observe(this@MainActivity) { position ->
            currentMoviePos = position
        }
        dataModel.mainToDetailFragMovie.observe(this@MainActivity) { movie ->
            currentMovie = movie
        }
        dataModel.actualMovieList.observe(this@MainActivity) { movieList ->
            currentMovieList =  movieList
        }


        //TopAppBar Settings and click listener
        with(binding) {
            topAppBar?.setNavigationOnClickListener {
                Snackbar.make(binding.mainLayout, "Navigation menu", Snackbar.LENGTH_SHORT).show()
            }
            topAppBar?.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        Snackbar.make(binding.mainLayout, "Settings", Snackbar.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }


        //Bottom Navigation Menu Settings and click listener
        binding.bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    //add starting fragment
                    addFragment(FavoriteFragment(),Constants.FAVORITE_FRAGMENT, R.id.fragmentContainerMain)
                    true
                }

                R.id.watch_later -> {
                    Snackbar.make(binding.mainLayout, "Watch Later", Snackbar.LENGTH_SHORT).show()
                    true
                }

                R.id.collection -> {
                    Snackbar.make(binding.mainLayout, "Film collection", Snackbar.LENGTH_SHORT)
                        .show()
                    true
                }

                else -> false
            }
        }
        // add some data to Recycler view
        val dataMovie = movieData()
        dataModel.actualMovieList.value = dataMovie
        //add starting fragment
        addFragment(MainFragment(),Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        //updating the current movie database
        val mainFragment = supportFragmentManager.findFragmentByTag(Constants.MAIN_FRAGMENT) as? MainFragment
        mainFragment?.updateDiffDataMovie(currentMovieList)

        //depending on which fragment in the stack we show the corresponding toolbar
        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount > 1) {


            val fragmentName = supportFragmentManager.getBackStackEntryAt(backStackCount - 2).name
            if (fragmentName == Constants.MAIN_FRAGMENT) {

                binding.topAppBar?.visibility = View.VISIBLE

            } else if (fragmentName == Constants.DETAIL_FRAGMENT) {
                binding.topAppBar?.visibility = View.GONE
            }
        } else if (backStackCount == 1) {
            binding.topAppBar?.visibility = View.VISIBLE

        } else {

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.dialog_title))
                .setMessage(resources.getString(R.string.dialog_supporting_text))
                .setNeutralButton(resources.getString(R.string.dialog_cancel)) { dialog, which ->
                    // Respond to neutral button press
                    addFragment(MainFragment(),Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
                    Snackbar.make(binding.mainLayout, "Welcome Again", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton(resources.getString(R.string.dialog_decline)) { dialog, which ->
                    // Respond to negative button press
                    addFragment(MainFragment(),Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
                    Snackbar.make(
                        binding.mainLayout,
                        "We're glad you're back",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                .setPositiveButton(resources.getString(R.string.dialog_accept)) { dialog, which ->
                    // Respond to positive button press
                    finish()
                }
                .show()
        }

    }

    private fun addFragment(fragment: Fragment,tag:String, container: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()

        if (fragment !is MainFragment) {
            binding.topAppBar?.visibility = View.GONE
        }

    }

    private fun movieData(): ArrayList<Movie> {
        val resultList = ArrayList<Movie>()
        repeat(9) {
            val index = it % MainFragment.imageIdList.size
            val movie =
                Movie(
                    (10000..99999).random() * (1 + it),
                    MainFragment.imageIdList[index],
                    MainFragment.titleList[index],
                    2023,
                    "It is a long established fact that a reader will be distracted " +
                            "by the readable content of a page when looking at its layout. " +
                            "The point of using Lorem Ipsum is that it has a more-or-less " +
                            "normal distribution of letters, as opposed to using.",
                    6.5,
                    it % 3 == 0
                )
            resultList.add(movie)
        }
        return resultList
    }

}