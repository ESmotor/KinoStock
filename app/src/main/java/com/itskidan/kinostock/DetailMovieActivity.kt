package com.itskidan.kinostock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itskidan.kinostock.databinding.ActivityDetailMovieBinding
import com.itskidan.kinostock.module.Movie

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //We accept and process the sent intent
        val intent = intent
        if (intent != null && intent.hasExtra("movie")) {
            //save the passed class into a variable
            val movie = intent.getParcelableExtra<Movie>("movie")
            //if the variable is not null, then we process its parameters in the way we need
            if (movie != null) {
                binding.toolbarLayout.title = movie.title
                binding.imageMovie.setImageResource(R.drawable.transformers)
                binding.tvMovieDescription.text = movie.description
                val releasedYearText = binding.tvMovieReleasedYear.text.toString() + movie.releaseYear
                binding.tvMovieReleasedYear.text = releasedYearText
            } else {
                binding.toolbarLayout.title = getString(R.string.error_title)
                binding.imageMovie.setImageResource(R.drawable.error)
                binding.tvMovieDescription.text = getString(R.string.error_description)
            }
        }

        //TopAppBar Settings
        topAppBarClickListener()



    }
    private fun showToastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    private fun topAppBarClickListener() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                val intent = Intent()
                intent.putExtra("key", "Result come")
                setResult(RESULT_OK,intent)
                finish()
//                showToastMsg("Navigation menu")
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        showToastMsg("Settings")
                        true
                    }
                    else -> false
                }
            }
        }
    }

}