package com.itskidan.kinostock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.itskidan.kinostock.databinding.FragmentDetailBinding
import com.itskidan.kinostock.viewModel.DataModel


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val dataModel: DataModel by activityViewModels()
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
        dataModel.dataMainFragToDetailFrag.observe(activity as LifecycleOwner) { movie ->
            //if the variable is not null, then we process its parameters in the way we need
            if (movie != null) {
                binding.toolbarLayout.title = movie.title
                binding.imageMovie.setImageResource(R.drawable.transformers)
                binding.tvMovieDescription.text = movie.description
                val releasedYearText =
                    binding.tvMovieReleasedYear.text.toString() + movie.releaseYear
                binding.tvMovieReleasedYear.text = releasedYearText
            } else {
                binding.toolbarLayout.title = getString(R.string.error_title)
                binding.imageMovie.setImageResource(R.drawable.error)
                binding.tvMovieDescription.text = getString(R.string.error_description)
            }
        }

        //TopAppBar Settings and click listener
        with(binding) {
            toolbar.setNavigationOnClickListener {
                Snackbar.make(binding.detailLayout, "Back to main page", Snackbar.LENGTH_SHORT).show()
            }
        }

        //Share floating bar click listener
        binding.fabShare.setOnClickListener {
            Snackbar.make(binding.detailLayout, "Share button", Snackbar.LENGTH_SHORT).show()
        }
    }


}