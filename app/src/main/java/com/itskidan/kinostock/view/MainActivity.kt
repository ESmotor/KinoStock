package com.itskidan.kinostock.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.R
import com.itskidan.kinostock.application.App
import com.itskidan.kinostock.databinding.ActivityMainBinding
import com.itskidan.kinostock.utils.Constants
import com.itskidan.kinostock.view.fragments.CollectionsFragment
import com.itskidan.kinostock.view.fragments.FavoriteFragment
import com.itskidan.kinostock.view.fragments.MainFragment
import com.itskidan.kinostock.view.fragments.WatchLaterFragment
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val receiver = ReceiverForEvent()
    private var currentFragment: Fragment? = Fragment()

    //    private val dataModel: UtilityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //put the observer in the activity we need
        lifecycle.addObserver(App.instance.lifecycleObserver)

        //add starting fragment
        addFragment(MainFragment(), Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
        //setup fragments manager and settings
        fragmentManagerSetup()
        // ReceiverForEvent
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_LOW)
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)

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
            currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerMain)
            Timber.tag("MyLog").d("currentFragment: ${currentFragment?.tag}")
            // In this block we will perform actions when changing the stack of fragments

            when (currentFragment) {
                is MainFragment -> {
                    Timber.tag("MyLog").d("MainFragment")
                    val botNavView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                    val homeItem = botNavView.menu.findItem(R.id.home)
                    homeItem.isChecked = true
                }

                is FavoriteFragment -> {
                    Timber.tag("MyLog").d("FavoriteFragment")
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

    inner class ReceiverForEvent : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // check Intent not null
            intent?.action?.let { action ->
                // Determine which event occurred based on the action
                when (action) {
                    Intent.ACTION_BATTERY_LOW -> {
                        // event low battery
                        Toast.makeText(applicationContext, "LOW BATTERY", Toast.LENGTH_SHORT).show()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    }

                    Intent.ACTION_POWER_CONNECTED -> {
                        // Event power connected
                        Toast.makeText(applicationContext, "POWER CONNECTED", Toast.LENGTH_SHORT)
                            .show()
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    //Add other actions as needed
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreateFragment()
    }
    private fun recreateFragment() {
        if (currentFragment != null) {
            when (currentFragment) {
                is MainFragment -> {
                    addFragment(MainFragment(), Constants.MAIN_FRAGMENT, R.id.fragmentContainerMain)
                }

                is FavoriteFragment -> {
                    addFragment(FavoriteFragment(), Constants.FAVORITE_FRAGMENT, R.id.fragmentContainerMain)
                }

                is WatchLaterFragment -> {
                    addFragment(WatchLaterFragment(), Constants.WATCH_LATER_FRAGMENT, R.id.fragmentContainerMain)
                }

                is CollectionsFragment -> {
                    addFragment(CollectionsFragment(), Constants.COLLECTIONS_FRAGMENT, R.id.fragmentContainerMain)
                }
            }
        }
    }

}