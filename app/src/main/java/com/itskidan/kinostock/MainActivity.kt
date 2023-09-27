package com.itskidan.kinostock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TopAppBar Settings and click listener
        with(binding) {
            topAppBar?.setNavigationOnClickListener {
                Snackbar.make(binding.mainLayout, "Navigation menu", Snackbar.LENGTH_SHORT).show()
            }
            topAppBar?.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        Snackbar.make(binding.mainLayout, "Settings", Snackbar.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }


        //Bottom Navigation Menu Settings and click listener
        binding.bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    Snackbar.make(binding.mainLayout, "Favorites", Snackbar.LENGTH_SHORT).show()
                    true
                }

                R.id.watch_later -> {
                    Snackbar.make(binding.mainLayout, "Watch Later", Snackbar.LENGTH_SHORT).show()
                    true
                }

                R.id.collection -> {
                    Snackbar.make(binding.mainLayout, "Film collection", Snackbar.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        //add starting fragment
        addFragment(MainFragment(), R.id.fragmentContainerMain)
    }


    override fun onBackPressed() {
        super.onBackPressed()
       //depending on which fragment in the stack we show the corresponding toolbar
        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount > 1) {
            val fragmentName = supportFragmentManager.getBackStackEntryAt(backStackCount - 2).name

            if (fragmentName == "mainFragment") {
                binding.topAppBar?.visibility = View.VISIBLE
            } else if (fragmentName == "detailFragment") {
                binding.topAppBar?.visibility = View.GONE
            }
        } else if (backStackCount == 1) {
            binding.topAppBar?.visibility = View.VISIBLE

        } else {

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.dialog_title))
                .setMessage(resources.getString(R.string.dialog_supporting_text))
                .setNeutralButton(resources.getString(R.string.dialog_cancel)) { dialog, which ->
                    // Respond to neutral button press
                    addFragment(MainFragment(), R.id.fragmentContainerMain)
                    Snackbar.make(binding.mainLayout, "Welcome Again", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton(resources.getString(R.string.dialog_decline)) { dialog, which ->
                    // Respond to negative button press
                    addFragment(MainFragment(), R.id.fragmentContainerMain)
                    Snackbar.make(binding.mainLayout, "We're glad you're back", Snackbar.LENGTH_SHORT).show()
                }
                .setPositiveButton(resources.getString(R.string.dialog_accept)) { dialog, which ->
                    // Respond to positive button press
                    finish()
                }
                .show()
        }

    }

    private fun addFragment(fragment: Fragment, container: Int) {
        val tag = "mainFragment"
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragment, tag)
            .addToBackStack(tag)
            .commit()

        if (fragment !is MainFragment) {
            binding.topAppBar?.visibility = View.GONE
        }

    }

}