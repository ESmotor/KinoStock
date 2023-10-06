package com.itskidan.kinostock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.databinding.FragmentDetailBinding
import com.itskidan.kinostock.module.Movie
import com.itskidan.kinostock.viewModel.DataModel
import kotlin.math.abs


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val dataModel: DataModel by activityViewModels()
    private var isOpen: Boolean = false
    private var currentMovie: Movie? = null
    private var currentMoviePos: Int? = null
    private var currentMovieList: ArrayList<Movie>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observing variables that may change
        dataModel.mainToDetailFragPosition.observe(activity as LifecycleOwner) { position ->
            currentMoviePos = position
        }
        dataModel.actualMovieList.observe(activity as LifecycleOwner) { movieList ->
            currentMovieList = movieList
        }
        dataModel.mainToDetailFragMovie.observe(activity as LifecycleOwner) { movie ->
            //if the variable is not null, then we process its parameters in the way we need
            if (movie != null) {
                currentMovie = movie
                binding.collapsingToolbarLayout.title = movie.title
                binding.imageMovie.setImageResource(R.drawable.transformers)
                binding.tvMovieDescription.text = movie.description
                binding.tvMovieReleasedYear.text = movie.releaseYear.toString()
            } else {
                binding.collapsingToolbarLayout.title = getString(R.string.error_title)
                binding.imageMovie.setImageResource(R.drawable.error)
                binding.tvMovieDescription.text = getString(R.string.error_description)
            }
            setUpAppBarLayout()
        }

        //setup toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)


        //TopAppBar Settings and click listener
        with(binding) {
            toolbar.setNavigationOnClickListener {
                Snackbar.make(binding.detailLayout, "Back to main page", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        //Share floating bar click listener
        binding.fabShare.setOnClickListener {
            Snackbar.make(binding.detailLayout, "Share button", Snackbar.LENGTH_SHORT).show()
        }
        //Share floating bar click listener
        binding.fabFav.setOnClickListener {
            onFavoriteClick()
        }

        // install menu to our toolbar
        val menuHost: MenuHost = requireActivity()
        // Add menu items without using the Fragment Menu APIs
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.top_fragment_menu, menu)
                // Find item menu isFavorite
                val isFavoriteMenuItem = menu.findItem(R.id.isFavorite)
                if (currentMovie!!.isFavorite) {
                    isFavoriteMenuItem.setIcon(R.drawable.ic_round_favorite_24)
                    binding.fabFav.setImageResource(R.drawable.ic_round_favorite_24)
                } else {
                    isFavoriteMenuItem.setIcon(R.drawable.ic_favorite_border_24)
                    binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
                }
                isFavoriteMenuItem.isVisible = false

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.isFavorite -> {
                        onFavoriteClick()
                        true
                    }

                    else -> false
                }
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    private fun setUpAppBarLayout() {

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange.toFloat()
            val percentage = abs(verticalOffset.toFloat())
            val favBtn = binding.toolbar.menu.findItem(R.id.isFavorite)
            val shareBtn = binding.toolbar.menu.findItem(R.id.share)
            favBtn.isVisible = (totalScrollRange == percentage)
            shareBtn.isVisible = (totalScrollRange == percentage)
            binding.pos1.text = isOpen.toString()
            binding.pos2.text = percentage.toString()
        })
    }

    private fun onFavoriteClick() {

        val menu = binding.toolbar.menu
        val isFavoriteMenuItem = menu.findItem(R.id.isFavorite)
        if (currentMovie != null && currentMovie!!.isFavorite) {
            currentMovie!!.isFavorite = false
            isFavoriteMenuItem.setIcon(R.drawable.ic_favorite_border_24)
            binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
        } else if (currentMovie != null && !(currentMovie!!.isFavorite)) {
            currentMovie!!.isFavorite = true
            isFavoriteMenuItem.setIcon(R.drawable.ic_round_favorite_24)
            binding.fabFav.setImageResource(R.drawable.ic_round_favorite_24)
        }

        currentMovieList?.set(currentMoviePos!!, currentMovie!!)

        dataModel.mainToDetailFragMovie.value = currentMovie
        dataModel.mainToDetailFragPosition.value = currentMoviePos
        dataModel.actualMovieList.value = currentMovieList

    }
}