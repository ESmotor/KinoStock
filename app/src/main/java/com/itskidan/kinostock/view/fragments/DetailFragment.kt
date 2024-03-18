package com.itskidan.kinostock.view.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.itskidan.core_api.entity.Film
import com.itskidan.core_impl.MainRepository
import com.itskidan.kinostock.R
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.databinding.FragmentDetailBinding
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.utils.ApiConstants
import com.itskidan.kinostock.utils.EnterFragmentAnimation
import com.itskidan.kinostock.viewmodel.DetailFragmentViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    // Initializing the interactor
    @Inject
    lateinit var interactor: Interactor

    // Initializing the repository
    @Inject
    lateinit var repository: MainRepository

    // Initializing launcher for permission
    private lateinit var pLauncher: ActivityResultLauncher<String>

    // Initializing for detail fragment viewModel
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailFragmentViewModel::class.java)
    }

    private lateinit var chosenFilm: Film

    private lateinit var detailFragmentScope: CoroutineScope
    private lateinit var exceptionHandler: CoroutineExceptionHandler

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

        registerPermissionListener()

        App.instance.dagger.inject(this)

        exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.tag("MyLog").d("Context: $coroutineContext, Error:${throwable.localizedMessage}")
            Toast.makeText(
                requireContext(),
                "An error occurred while loading the image. Try again.",
                Toast.LENGTH_SHORT
            ).show()

        }
        detailFragmentScope = CoroutineScope(Dispatchers.IO)

        receiveBundle()


        //val pagerAdapter = ScreenSlidePagerAdapter(requireActivity().supportFragmentManager)
        //binding.viewPagerContainer.adapter = pagerAdapter
        // binding.viewPagerContainer.setPageTransformer(true, ZoomPageTransformer())

        putPoster()

        // create enter animation for fragments like CircularRevealAnimation
        // Checking if the rootView is attached to the activity window
        if (!binding.detailLayout.isAttachedToWindow) {
            EnterFragmentAnimation.performFragmentCircularRevealAnimation(
                binding.detailLayout,
                requireActivity(),
                1
            )
        } else{
            binding.detailLayout.visibility = View.VISIBLE
        }


        // TopAppBar Setup
        setUpAppBarLayout()
        // Share floating bar click listener
        binding.fabShare.setOnClickListener {
            viewModel.onShareClick(requireContext(), chosenFilm)
        }
        // Favorite floating bar click listener
        binding.fabFav.setOnClickListener {
            onFavoriteClick()
        }
        // Download floating button click listener
        binding.detailsFabDownloadWp.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 33) {
                checkPermission(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

        }
    }


//    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
//        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//        override fun getCount(): Int = NUM_PAGES
//        override fun getItem(position: Int): Fragment = PageFragment(position)
//    }

//    companion object {
//        const val NUM_PAGES = 5
//    }

    private fun putPoster() {
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + chosenFilm.poster)
            .apply(RequestOptions().placeholder(R.drawable.poster_error_404))
            .centerCrop()
            .into(binding.detailsPoster)
    }

    private fun receiveBundle() {
        chosenFilm = arguments?.getParcelable("film")
            ?: Film(
                id = 0,
                title = "Film not come",
                poster = "Something Wrong",
                description = "Something Wrong",
                rating = 10.0,
                releaseDate = "01.01.01",
                isInFavorites = false
            )
        binding.collapsingToolbarLayout.title = chosenFilm.title
        binding.tvMovieDescription.text = chosenFilm.description
        binding.tvMovieReleasedYear.text = chosenFilm.releaseDate
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
                if (chosenFilm.isInFavorites) {
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
                        viewModel.onShareClick(requireContext(), chosenFilm)
                        true
                    }

                    else -> false
                }
            }


        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    // registration permission listener for request
    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                Timber.tag("MyLog").d("Permission Granted")
            } else {
                Timber.tag("MyLog").d("Permission denied")
            }
        }
    }

    // Let's find out if permission was received earlier
    private fun checkPermission(nameOfPermission: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                nameOfPermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                Timber.tag("MyLog").d("The required permission is already available.")
                performAsyncLoadOfPoster()
            }

            shouldShowRequestPermissionRationale(nameOfPermission) -> {
                Timber.tag("MyLog").d("Need this permission for save media files.")
            }

            else -> {
                pLauncher.launch(nameOfPermission)
            }
        }
    }

    private fun saveToGallery(bitmap: Bitmap) {
        // Checking the system version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Create an object for data transfer
            val contentValues = ContentValues().apply {
                // We compile information for the file (name, type, creation date, where to save, etc.)
                put(MediaStore.Images.Media.TITLE, chosenFilm.title.handleSingleQuote())
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    chosenFilm.title.handleSingleQuote()
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.DATE_ADDED,
                    System.currentTimeMillis() / 1000
                )
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmsSearchApp")
            }
            // We get a link to the Content resolver object, which helps pass information from the application outside
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            // Opening a channel for recording to disk
            val outputStream = contentResolver.openOutputStream(uri!!)
            // We transmit our image, it can be compressed
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            // Closing the stream
            outputStream?.close()
        } else {
            // The same, but for older OS versions
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                chosenFilm.title.handleSingleQuote(),
                chosenFilm.description.handleSingleQuote()
            )
        }
    }

    private fun performAsyncLoadOfPoster() {
        // We create a parent scope with the Main thread dispatcher, since we will interact with the UI
        Timber.tag("MyLog").d("We have permission, starting work")
        MainScope().launch(exceptionHandler) {
            // Turn on the Progress Bar
            binding.progressBar.isVisible = true
            // We create it via async, since we need the result of the work, that is, Bitmap
            val job = detailFragmentScope.async {
                viewModel.loadWallpaper(ApiConstants.IMAGES_URL + "original" + chosenFilm.poster)
            }
            // Save to the gallery as soon as the file is downloaded
            saveToGallery(job.await())
            // We display a snackbar with a button to go to the gallery
            Snackbar.make(
                binding.root,
                R.string.downloaded_to_gallery,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.open) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.type = "image/*"
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()

            // Disable the Progress Bar
            binding.progressBar.isVisible = false
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }

    private fun onFavoriteClick() {
        val menu = binding.toolbar.menu
        val isFavoriteMenuItem = menu.findItem(R.id.isFavorite)
        if (chosenFilm.isInFavorites) {
            chosenFilm.isInFavorites = false
            isFavoriteMenuItem.setIcon(R.drawable.ic_favorite_border_24)
            binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
            repository.removeFromFavoriteFilm(chosenFilm)

        } else {
            chosenFilm.isInFavorites = true
            isFavoriteMenuItem.setIcon(R.drawable.ic_round_favorite_24)
            binding.fabFav.setImageResource(R.drawable.ic_round_favorite_24)
            repository.setAsFavoriteFilm(chosenFilm)
        }
    }

}