package com.itskidan.kinostock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.itskidan.kinostock.adapters.PosterAdapter
import com.itskidan.kinostock.databinding.FragmentMainBinding
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.module.Poster
import com.itskidan.kinostock.viewModel.DataModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val dataModel: DataModel by activityViewModels()
    lateinit var movieAdapter: MovieAdapter
    lateinit var posterAdapter: PosterAdapter

    lateinit var currentMovieList: ArrayList<Movie>
    var currentMovie: Movie? = null
    var currentMoviePos: Int? = null

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

        //Movie List Recycler View
        //create main Movie Adapter with click listener on items
        movieAdapterSetup()
        // Top Posters Recycler View
        //create Poster Adapter
        posterAdapterSetup()
        // add some data to Recycler view
        val dataPoster = posterData()
        posterAdapter.addAllPoster(dataPoster)

        //TopAppBar Setup
        topAppBarSetup()
        //BottomNavigationBar setup
        bottomNavigationBarSetup()
        //Observing requires data
        dataModelObserving()
    }

    //Main movie Adapter Setup
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
        binding.rvMoovieList.adapter = movieAdapter
        binding.rvMoovieList.layoutManager = layoutManagerMovie
        binding.rvMoovieList.addItemDecoration(movieItemsDecoration)
    }

    //Poster Adapter Setup
    private fun posterAdapterSetup() {
        posterAdapter = PosterAdapter()
        val layoutManagerPoster =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvPoster.adapter = posterAdapter
        binding.rvPoster.layoutManager = layoutManagerPoster
    }

    //TopAppBar Settings and click listener
    private fun topAppBarSetup() {
        binding.topAppBar.setNavigationOnClickListener {
            Snackbar.make(binding.mainLayout, "Navigation menu", Snackbar.LENGTH_SHORT).show()
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Snackbar.make(binding.mainLayout, "Settings", Snackbar.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    //BottomNavigationBar Settings and click listener
    private fun bottomNavigationBarSetup() {
        binding.bottomNavigation.selectedItemId = R.id.home
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    addFragment(
                        MainFragment(),
                        Constants.MAIN_FRAGMENT,
                        R.id.fragmentContainerMain
                    )
                    true
                }

                R.id.favorites -> {
                    addFragment(
                        FavoriteFragment(),
                        Constants.FAVORITE_FRAGMENT,
                        R.id.fragmentContainerMain
                    )
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
    }

    //update data with DiffUtil
    fun updateDiffDataMovie(newData: ArrayList<Movie>) {
        val oldData = movieAdapter.data
        val movieDiff = DiffMovieAdapter(oldData, newData)
        val diffResult = DiffUtil.calculateDiff(movieDiff)
        movieAdapter.data = newData
        diffResult.dispatchUpdatesTo(movieAdapter)
    }

    //Function for adding fragments
    private fun addFragment(fragment: Fragment, tag: String, container: Int) {
        val activity = requireActivity()
        activity.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }


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
    val subtitleList = listOf(
        "Action",
        "Horror",
        "Fantastic",
        "Cartoon"
    )
}

}