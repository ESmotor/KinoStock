package com.itskidan.kinostock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
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
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnItemClickListener {
            override fun click(movie: Movie) {
                //reaction to a click on a Recycler View element
                dataModel.dataMainFragToDetailFrag.value = movie
                addFragment(DetailFragment(), R.id.fragmentContainerMain)
            }
        })

        // create LayoutManager
        val layoutManagerMovie =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        // create ItemDecoration for offset
        val movieItemsDecoration = MovieItemsDecoration(8)
        //setup Movie Adapter to our Recycler view
        binding.rvMoovieList.apply {
            this.adapter = movieAdapter
            this.layoutManager = layoutManagerMovie
            this.addItemDecoration(movieItemsDecoration)
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
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        //setup Movie Adapter to our Recycler view
        binding.rvPoster.apply {
            this.adapter = posterAdapter
            this.layoutManager = layoutManagerPoster
        }
        // add some data to Recycler view
        val dataPoster = posterData()
        posterAdapter.addAllPoster(dataPoster)

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

    private fun addFragment(fragment: Fragment, container: Int) {
        val tag = "detailFragment"
        val activity = requireActivity()
        val toolbar = activity.findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.visibility = View.GONE
        activity.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
        if (fragment !is MainFragment){
            toolbar.visibility = View.GONE
        }
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