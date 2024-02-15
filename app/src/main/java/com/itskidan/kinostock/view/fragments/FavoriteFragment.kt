package com.itskidan.kinostock.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.FragmentFavoriteBinding
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.domain.OnItemClickListener
import com.itskidan.kinostock.utils.Constants
import com.itskidan.kinostock.utils.EnterFragmentAnimation
import com.itskidan.kinostock.view.rv_adapters.MovieItemsDecoration
import com.itskidan.kinostock.viewmodel.FavoriteFragmentViewModel
import com.itskidan.kinostock.viewmodel.UtilityViewModel
import com.itskidan.myapplication.ModelItemDiffAdapter
import timber.log.Timber


class FavoriteFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    private val modelAdapter = ModelItemDiffAdapter(this)

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoriteFragmentViewModel::class.java)
    }

    private val utilityViewModel: UtilityViewModel by activityViewModels()
    private var actualFilmList = ArrayList<Film>()
    private var favoriteFilmList = ArrayList<Film>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.filmsListLiveData.observe(viewLifecycleOwner) {

        }
        EnterFragmentAnimation.performFragmentCircularRevealAnimation(
            binding.favoritesLayout,
            requireActivity(),
            2
        )
        // Movie List Recycler View
        // create main Movie Adapter with click listener on items
        movieAdapterSetup()

        // TopAppBar Setup
        topAppBarSetup()

        // BottomNavigationBar setup
        bottomNavigationBarSetup()

        // Setup Searching menu and icon
        onCreateSearchingMenu()

        dataModelObserving()
    }

    // Function for using searching icon and view and changing data
    private fun onCreateSearchingMenu() {
        val menu = binding.topAppBar.menu
        val menuItemSearch = menu.findItem(R.id.search)
        val searchView = menuItemSearch.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newFavoriteDataList = viewModel.handleSearch(newText, favoriteFilmList)
                modelAdapter.updateItems(newFavoriteDataList)
                return false
            }
        })
    }

    // Main movie Adapter Setup
    private fun movieAdapterSetup() {
        // create LayoutManager
        val layoutManagerMovie =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        // create ItemDecoration for offset
        val movieItemsDecoration = MovieItemsDecoration(8)
        //setup Movie Adapter to our Recycler view
        val rvMovieList = requireView().findViewById<RecyclerView>(R.id.rv_movie_list_fav)
        rvMovieList.adapter = modelAdapter
        rvMovieList.layoutManager = layoutManagerMovie
        rvMovieList.addItemDecoration(movieItemsDecoration)
    }


    // TopAppBar Settings and click listener
    private fun topAppBarSetup() {
        binding.topAppBar.setNavigationOnClickListener {
            Snackbar.make(binding.favoritesLayout, "Navigation menu", Snackbar.LENGTH_SHORT).show()
        }
        binding.topAppBar.setOnMenuItemClickListener {
            true
        }
    }

    // BottomNavigationBar Settings and click listener
    private fun bottomNavigationBarSetup() {
        binding.bottomNavigation.selectedItemId = R.id.favorites
        binding.bottomNavigation.setOnItemSelectedListener {
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
                        fragment ?: CollectionsFragment(),
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
    private fun addFragment(fragment: Fragment, tag: String, container: Int) {
        val activity = requireActivity()
        activity.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun dataModelObserving() {
        utilityViewModel.actualFilmList.observe(activity as LifecycleOwner) { list ->
            actualFilmList = list
            Timber.tag("Mylog").d("actualFilmListSize = ${actualFilmList.size}")

        }
        utilityViewModel.favoriteFilmList.observe(activity as LifecycleOwner) { list ->
            favoriteFilmList = list
            modelAdapter.updateItems(favoriteFilmList)
        }
    }

    override fun onItemClick(film: Film) {
        //reaction to a click on a Recycler View element
        utilityViewModel.chosenFilm.value = film
        utilityViewModel.chosenMoviePosition.value = actualFilmList.indexOf(film)
        addFragment(DetailFragment(), Constants.DETAIL_FRAGMENT, R.id.fragmentContainerMain)
    }
}