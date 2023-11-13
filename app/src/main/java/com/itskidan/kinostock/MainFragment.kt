package com.itskidan.kinostock


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.adapters.DiffMovieAdapter
import com.itskidan.kinostock.adapters.MovieAdapter
import com.itskidan.kinostock.adapters.MovieItemsDecoration
import com.itskidan.kinostock.databinding.FragmentMainBinding
import com.itskidan.kinostock.detailpage.DetailFragment
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.viewModel.DataModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val dataModel: DataModel by activityViewModels()
    private lateinit var movieAdapter: MovieAdapter


    private lateinit var currentMovieList: ArrayList<Movie>
    private var currentMovie: Movie? = null
    private var currentMoviePos: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and make ViewBinding
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // create enter animation for fragments like CircularRevealAnimation
        EnterFragmentAnimation.performFragmentCircularRevealAnimation(binding.rootMainFragment, requireActivity(), 1)
        // Movie List Recycler View
        // create main Movie Adapter with click listener on items
        movieAdapterSetup()

        // TopAppBar Setup
        topAppBarSetup()

        // BottomNavigationBar setup
        bottomNavigationBarSetup()

        // Setup Searching menu and icon
        onCreateSearchingMenu()

        // Observing requires data
        dataModelObserving()
    }

    // Function for using searching icon and view and changing data
    private fun onCreateSearchingMenu() {
        val topToolbar = requireView().findViewById<MaterialToolbar>(R.id.topToolbar)
        val menu = topToolbar.menu
        val menuItemSearch = menu.findItem(R.id.search)
        val searchView = menuItemSearch.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrEmpty()) {
                    val newDataList = currentMovieList
                    updateDiffDataMovie(newDataList)
                } else {
                    val newDataList = ArrayList<Movie>(currentMovieList.filter {
                        it.title.contains(
                            newText,
                            true
                        )
                    })
                    updateDiffDataMovie(newDataList)
                }

                return false
            }
        })
    }


    // Main movie Adapter Setup
    private fun movieAdapterSetup() {
        movieAdapter = MovieAdapter(object : MovieAdapter.OnItemClickListener {
            override fun click(movie: Movie, position: Int) {
                //reaction to a click on a Recycler View element
                dataModel.mainToDetailFragMovie.value = movie
                dataModel.mainToDetailFragPosition.value = position
                addFragment(DetailFragment(), Constants.DETAIL_FRAGMENT, R.id.fragmentContainerMain)
            }
        })
        // create LayoutManager
        val layoutManagerMovie =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        // create ItemDecoration for offset
        val movieItemsDecoration = MovieItemsDecoration(8)
        //setup Movie Adapter to our Recycler view
        val rvMovieList = requireView().findViewById<RecyclerView>(R.id.rvMovieList)
        rvMovieList.adapter = movieAdapter
        rvMovieList.layoutManager = layoutManagerMovie
        rvMovieList.addItemDecoration(movieItemsDecoration)
    }

    // TopAppBar Settings and click listener
    private fun topAppBarSetup() {
        val topToolbar = requireView().findViewById<MaterialToolbar>(R.id.topToolbar)
        topToolbar.setNavigationOnClickListener {
            Snackbar.make(binding.rootMainFragment, "Navigation menu", Snackbar.LENGTH_SHORT).show()
        }
        topToolbar.setOnMenuItemClickListener {
            true
        }
    }

    // BottomNavigationBar Settings and click listener
    private fun bottomNavigationBarSetup() {
        val botNav = requireView().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        botNav.selectedItemId = R.id.home
        botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val fragment: Fragment? = requireActivity()
                        .supportFragmentManager
                        .findFragmentByTag(Constants.MAIN_FRAGMENT)
                    addFragment(
                        fragment ?: MainFragment(),
                        Constants.MAIN_FRAGMENT,
                        R.id.fragmentContainerMain
                    )
                    true
                }

                R.id.favorites -> {
                    val fragment: Fragment? = requireActivity()
                        .supportFragmentManager
                        .findFragmentByTag(Constants.FAVORITE_FRAGMENT)
                    addFragment(
                        fragment ?: FavoriteFragment(),
                        Constants.FAVORITE_FRAGMENT,
                        R.id.fragmentContainerMain
                    )
                    true
                }

                R.id.watch_later -> {
                    val fragment: Fragment? = requireActivity()
                        .supportFragmentManager
                        .findFragmentByTag(Constants.WATCH_LATER_FRAGMENT)
                    addFragment(
                        fragment ?: WatchLaterFragment(),
                        Constants.WATCH_LATER_FRAGMENT,
                        R.id.fragmentContainerMain
                    )
                    true
                }

                R.id.collection -> {
                    val fragment: Fragment? = requireActivity()
                        .supportFragmentManager
                        .findFragmentByTag(Constants.COLLECTIONS_FRAGMENT)
                    addFragment(
                        fragment ?:CollectionsFragment(),
                        Constants.COLLECTIONS_FRAGMENT,
                        R.id.fragmentContainerMain
                    )
                    true
                }

                else -> false
            }
        }
    }

    // update data with DiffUtil
    private fun updateDiffDataMovie(newData: ArrayList<Movie>) {
        val oldData = movieAdapter.data
        val movieDiff = DiffMovieAdapter(oldData, newData)
        val diffResult = DiffUtil.calculateDiff(movieDiff)
        movieAdapter.data = newData
        diffResult.dispatchUpdatesTo(movieAdapter)
    }

    // Function for adding fragments
    private fun addFragment(fragment: Fragment, tag: String, container: Int) {
        val activity = requireActivity()
        activity.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    // Function for observing of data which was changed in other fragments
    private fun dataModelObserving() {
        dataModel.mainToDetailFragPosition.observe(activity as LifecycleOwner) { position ->
            currentMoviePos = position
        }
        dataModel.mainToDetailFragMovie.observe(activity as LifecycleOwner) { movie ->
            currentMovie = movie
        }
        dataModel.actualMovieList.observe(activity as LifecycleOwner) { movieList ->
            currentMovieList = movieList
            updateDiffDataMovie(currentMovieList)
        }
    }

    companion object {
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
    }

}