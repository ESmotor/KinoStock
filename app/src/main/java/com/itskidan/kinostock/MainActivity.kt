package com.itskidan.kinostock

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.databinding.ActivityMainBinding
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.viewModel.DataModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentMoviePos: Int? = null
    private var currentMovie: Movie? = null
    private lateinit var currentMovieList: ArrayList<Movie>
    private val dataModel: DataModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //put the observer in the activity we need
        lifecycle.addObserver(App.instance.lifecycleObserver)
        //Monitoring changes in variables in the DataModel class
        dataModelObserving()
        // add some data to Recycler view
        val dataMovie = movieData()
        dataModel.actualMovieList.value = dataMovie
        //add starting fragment
        addFragment(MainFragment(), Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
        //setup fragments manager and settings
        fragmentManagerSetup()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        //depending on which fragment in the stack we show the corresponding toolbar
        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount < 1) {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.dialog_title))
                .setMessage(resources.getString(R.string.dialog_supporting_text))
                .setNeutralButton(resources.getString(R.string.dialog_cancel)) { _, _ ->
                    // Respond to neutral button press
                    addFragment(MainFragment(), Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
                    Snackbar.make(
                        binding.fragmentContainerMain,
                        "Welcome Again",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(resources.getString(R.string.dialog_decline)) { _, _ ->
                    // Respond to negative button press
                    addFragment(MainFragment(), Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
                    Snackbar.make(
                        binding.fragmentContainerMain,
                        "We're glad you're back",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                .setPositiveButton(resources.getString(R.string.dialog_accept)) { _, _ ->
                    // Respond to positive button press
                    finish()
                }
                .show()
        }

    }

    // function to track changes in the fragment manager
    private fun fragmentManagerSetup() {
        supportFragmentManager.addOnBackStackChangedListener {
            // In this block we will perform actions when changing the stack of fragments
            when (supportFragmentManager.findFragmentById(R.id.fragmentContainerMain)) {
                is MainFragment -> {
                    val botNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                    val homeItem = botNavView.menu.findItem(R.id.home)
                    homeItem.isChecked = true
                }

                is FavoriteFragment -> {
                    val botNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                    val favoriteItem = botNavView.menu.findItem(R.id.favorites)
                    favoriteItem.isChecked = true
                }

                is WatchLaterFragment -> {
                    val botNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                    val watchLaterItem = botNavView.menu.findItem(R.id.watch_later)
                    watchLaterItem.isChecked = true
                }

                is CollectionsFragment -> {
                    val botNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                    val collectionsItem = botNavView.menu.findItem(R.id.collection)
                    collectionsItem.isChecked = true
                }
            }
        }
    }

    private fun addFragment(fragment: Fragment, tag: String, container: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
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
                    (10..100).random(),
                    it % 3 == 0
                )
            resultList.add(movie)
        }
        return resultList
    }

    private fun dataModelObserving() {
        dataModel.mainToDetailFragPosition.observe(this@MainActivity) { position ->
            currentMoviePos = position
        }
        dataModel.mainToDetailFragMovie.observe(this@MainActivity) { movie ->
            currentMovie = movie
        }
        dataModel.actualMovieList.observe(this@MainActivity) { movieList ->
            currentMovieList = movieList
        }
    }
}