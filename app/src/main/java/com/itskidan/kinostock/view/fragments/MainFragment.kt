package com.itskidan.kinostock.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.R
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.data.MainRepository
import com.itskidan.kinostock.data.entity.Film
import com.itskidan.kinostock.databinding.FragmentMainBinding
import com.itskidan.kinostock.domain.AutoDisposable
import com.itskidan.kinostock.domain.Interactor
import com.itskidan.kinostock.domain.OnItemClickListener
import com.itskidan.kinostock.domain.addTo
import com.itskidan.kinostock.paging.PaginationScrollListener
import com.itskidan.kinostock.utils.Constants
import com.itskidan.kinostock.utils.EnterFragmentAnimation
import com.itskidan.kinostock.view.rv_adapters.MovieItemsDecoration
import com.itskidan.kinostock.viewmodel.MainFragmentViewModel
import com.itskidan.myapplication.ModelItemDiffAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class MainFragment : Fragment(), OnItemClickListener {


    @Inject
    lateinit var repository: MainRepository

    @Inject
    lateinit var interactor: Interactor

    private lateinit var binding: FragmentMainBinding
    private var isLoading = false
    private val modelAdapter = ModelItemDiffAdapter(this)
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainFragmentViewModel::class.java)
    }
    private var filmsDataBase = ArrayList<Film>()
//        //Use backing field
//        set(value) {
//            //If the same value comes, then we exit the method
//            if (field == value) return
//            //If a different value arrives, then we put it in a variable
//            field = value
//            addData(field)
//        }

    private var isUpdated: Boolean = false

    //parameters for RxJava
    private val autoDisposable = AutoDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and make ViewBinding
        binding = FragmentMainBinding.inflate(inflater)
        autoDisposable.bindTo(lifecycle)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.instance.dagger.inject(this)

        // checking whether the progress bar needs to be shown
        viewModel.progressBarSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> binding.progressBar.isVisible = result },
                { error -> println("Error: ${error.localizedMessage}") }
            ).addTo(autoDisposable)

        // If there are problems with receiving data through the API, we display the message once
        viewModel.connectionProblemEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        // create enter animation for fragments like CircularRevealAnimation
        EnterFragmentAnimation.performFragmentCircularRevealAnimation(
            binding.rootMainFragment,
            requireActivity(),
            1
        )

        // Movie List Recycler View
        // create main Movie Adapter with click listener on items
        movieAdapterSetup()
        addScrollListener()

        // TopAppBar Setup
        topAppBarSetup()

        // BottomNavigationBar setup
        bottomNavigationBarSetup()

        // Setup Searching menu and icon
        onCreateSearchingMenu()

        // observeReceiveData()
        receiveDatabaseWithRxJava()


    }

    private fun receiveDatabaseWithRxJava() {

        if (!isUpdated && viewModel.isDatabaseUpdateTime(1)) {
            Timber.tag("MyLog").d("Loading from the API, clear database")
            val notFavFilmList = filmsDataBase.filter { film ->
                !film.isInFavorites
            }
            repository.clearDB(ArrayList(notFavFilmList))
            viewModel.getFilms()
            isUpdated = !isUpdated
        }

        viewModel.databaseFromDB
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> modelAdapter.updateItems(ArrayList(result)) },
                { error -> println("Error: ${error.localizedMessage}") }
            ).addTo(autoDisposable)

        Timber.tag("MyLog").d("dataSize = ${modelAdapter.items.size}")
        isLoading = false
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
                val newDataList = viewModel.handleSearch(filmsDataBase, newText)
                modelAdapter.updateItems(newDataList)
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
        val rvMovieList = requireView().findViewById<RecyclerView>(R.id.rvMovieList)
        rvMovieList.adapter = modelAdapter
        rvMovieList.layoutManager = layoutManagerMovie
        rvMovieList.addItemDecoration(movieItemsDecoration)

    }

    private fun addScrollListener() {
        binding.rvMovieList.addOnScrollListener(object :
            PaginationScrollListener(binding.rvMovieList.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                viewModel.getFilms()
            }

            override fun isLastPage() = viewModel.isAllPagesLoaded

            override fun isLoading() = isLoading
        })
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

    // Function for adding fragments
    private fun addFragment(fragment: Fragment, tag: String, container: Int, film: Film? = null) {
        val activity = requireActivity()
        film?.let {
            val bundle = Bundle().apply {
                putParcelable("film", it)
            }
            fragment.arguments = bundle
        }

        activity.supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    override fun onItemClick(film: Film) {
        //reaction to a click on a Recycler View element
        addFragment(DetailFragment(), Constants.DETAIL_FRAGMENT, R.id.fragmentContainerMain, film)
    }
}