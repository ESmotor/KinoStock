package com.itskidan.kinostock

import android.content.Intent
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

    lateinit var currentMovieList: ArrayList<Movie>
    var currentMovie: Movie? = null
    var currentMoviePos: Int? = null

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

        //TopAppBar Setup
        setUpAppBarLayout()
        //Share floating bar click listener
        binding.fabShare.setOnClickListener {
            onShareClick()
        }
        //Favorite floating bar click listener
        binding.fabFav.setOnClickListener {
            onFavoriteClick()
        }
        //Observing requires data
        dataModelObserving()
    }


    private fun setUpAppBarLayout() {
        //setup toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        //TopAppBar Settings and click listener
        toolbar.setNavigationOnClickListener {
            Snackbar.make(binding.detailLayout, "Back to main page", Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange.toFloat()
            val percentage = abs(verticalOffset.toFloat())
            val favBtn = binding.toolbar.menu.findItem(R.id.isFavorite)
            val shareBtn = binding.toolbar.menu.findItem(R.id.share)
            favBtn?.isVisible = (totalScrollRange == percentage)
            shareBtn?.isVisible = (totalScrollRange == percentage)
        })

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

                    R.id.share -> {
                        onShareClick()
                        true
                    }

                    else -> false
                }
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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

        currentMovieList[currentMoviePos!!] = currentMovie!!

        dataModel.mainToDetailFragMovie.value = currentMovie
        dataModel.mainToDetailFragPosition.value = currentMoviePos
        dataModel.actualMovieList.value = currentMovieList

    }

    private fun onShareClick() {
        //Create an intent
        val intent = Intent()
        //Specify the action with which it is launched
        intent.action = Intent.ACTION_SEND
        //Put data about our movie
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Check out this film: ${currentMovie?.title} \n\n ${currentMovie?.description}"
        )
        //Specify the MIME type so that the system knows which application to offer
        intent.type = "text/plain"
        //Launch our activity
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    private fun dataModelObserving() {
        dataModel.mainToDetailFragPosition.observe(activity as LifecycleOwner) { position ->
            currentMoviePos = position
        }
        dataModel.mainToDetailFragMovie.observe(activity as LifecycleOwner) { movie ->
            currentMovie = movie
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
        }
        dataModel.actualMovieList.observe(activity as LifecycleOwner) { movieList ->
            currentMovieList = movieList
        }
    }

}