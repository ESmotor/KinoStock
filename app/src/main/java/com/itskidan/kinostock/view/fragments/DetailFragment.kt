package com.itskidan.kinostock.view.fragments

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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.itskidan.kinostock.R
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.database.DatabaseHelper
import com.itskidan.kinostock.databinding.FragmentDetailBinding
import com.itskidan.kinostock.domain.Film
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.utils.ApiConstants
import com.itskidan.kinostock.utils.EnterFragmentAnimation
import com.itskidan.kinostock.viewmodel.DetailFragmentViewModel
import com.itskidan.kinostock.viewmodel.UtilityViewModel
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs


class DetailFragment  : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val utilityViewModel: UtilityViewModel by activityViewModels()
    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor
    @Inject
    lateinit var repository: MainRepository



    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailFragmentViewModel::class.java)
    }
    private var filmsDataBase = ArrayList<Film>()
    private var favoriteFilmList = ArrayList<Film>()

    var chosenFilm: Film? = null
    var chosenMoviePosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        App.instance.dagger.inject(this)

        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<ArrayList<Film>> {
            filmsDataBase = it
        })
        // Observing requires data
        dataModelObserving()

        //val pagerAdapter = ScreenSlidePagerAdapter(requireActivity().supportFragmentManager)
        //binding.viewPagerContainer.adapter = pagerAdapter
        // binding.viewPagerContainer.setPageTransformer(true, ZoomPageTransformer())

        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + chosenFilm?.poster)
            .centerCrop()
            .into(binding.detailsPoster)

        // create enter animation for fragments like CircularRevealAnimation
        EnterFragmentAnimation.performFragmentCircularRevealAnimation(
            binding.detailLayout,
            requireActivity(),
            2
        )
        // TopAppBar Setup
        setUpAppBarLayout()
        // Share floating bar click listener
        binding.fabShare.setOnClickListener {
            viewModel.onShareClick(requireContext(), chosenFilm!!)
        }
        // Favorite floating bar click listener
        binding.fabFav.setOnClickListener {
            onFavoriteClick()
        }

        // log
        Timber.tag("MyLog").d("Position = ${chosenMoviePosition}")
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES
        override fun getItem(position: Int): Fragment = PageFragment(position)
    }

    companion object {
        const val NUM_PAGES = 5
    }

    private fun setUpAppBarLayout() {
        //setup toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        //TopAppBar Settings and click listener
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange.toFloat()
            val percentage = abs(verticalOffset.toFloat())
            val favBtn = binding.toolbar.menu.findItem(R.id.isFavorite)
            val shareBtn = binding.toolbar.menu.findItem(R.id.share)
            favBtn?.isVisible = (totalScrollRange == percentage)
            shareBtn?.isVisible = (totalScrollRange == percentage)
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
                if (chosenFilm!!.isInFavorites) {
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
                        viewModel.onShareClick(requireContext(), chosenFilm!!)
                        true
                    }

                    else -> false
                }
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun onFavoriteClick() {
        Timber.tag("MyLog").d("On method come ")
        val menu = binding.toolbar.menu
        val isFavoriteMenuItem = menu.findItem(R.id.isFavorite)
        if (chosenFilm != null && chosenFilm!!.isInFavorites) {
            Timber.tag("MyLog").d("Step 1")
            chosenFilm!!.isInFavorites = false
            isFavoriteMenuItem.setIcon(R.drawable.ic_favorite_border_24)
            binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
            if (favoriteFilmList.contains(chosenFilm)) {
                Timber.tag("MyLog").d("Step 2")
                repository.deleteFromDb(chosenFilm!!,DatabaseHelper.TABLE_FAVORITES_NAME)
                favoriteFilmList.remove(chosenFilm)
            }
        } else if (chosenFilm != null && !(chosenFilm!!.isInFavorites)) {
            Timber.tag("MyLog").d("Step 3")
            chosenFilm!!.isInFavorites = true
            isFavoriteMenuItem.setIcon(R.drawable.ic_round_favorite_24)
            binding.fabFav.setImageResource(R.drawable.ic_round_favorite_24)
            if (!(favoriteFilmList.contains(chosenFilm))) {
                repository.putToDb(chosenFilm!!,DatabaseHelper.TABLE_FAVORITES_NAME)
                favoriteFilmList.add(chosenFilm!!)
            }
        }

        utilityViewModel.favoriteFilmList.value = favoriteFilmList
        utilityViewModel.chosenFilm.value = chosenFilm
        utilityViewModel.chosenMoviePosition.value = chosenMoviePosition

    }

    private fun dataModelObserving() {
        utilityViewModel.chosenMoviePosition.observe(activity as LifecycleOwner) { position ->
            chosenMoviePosition = position
        }
        utilityViewModel.favoriteFilmList.observe(activity as LifecycleOwner) { list ->
            favoriteFilmList = list
        }
        utilityViewModel.chosenFilm.observe(activity as LifecycleOwner) { film ->
            chosenFilm = film
            //if the variable is not null, then we process its parameters in the way we need
            if (film != null) {
                chosenFilm = film
                binding.collapsingToolbarLayout.title = film.title
                //binding.imageMovie.setImageResource(R.drawable.transformers)
                binding.tvMovieDescription.text = film.description
                binding.tvMovieReleasedYear.text = film.releaseDate.toString()
            } else {
                binding.collapsingToolbarLayout.title = getString(R.string.error_title)
                //binding.imageMovie.setImageResource(R.drawable.error)
                binding.tvMovieDescription.text = getString(R.string.error_description)
            }
        }

    }

}