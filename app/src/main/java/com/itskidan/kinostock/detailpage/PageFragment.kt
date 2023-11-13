package com.itskidan.kinostock.detailpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itskidan.kinostock.R
import com.itskidan.kinostock.databinding.ZoomViewPagerBinding

class PageFragment (var position: Int) : Fragment() {
    val images = arrayOf(
        R.drawable.movie_slide1,
        R.drawable.movie_slide2,
        R.drawable.movie_slide3,
        R.drawable.movie_slide4,
        R.drawable.movie_slide5
    )
    lateinit var binding: ZoomViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ZoomViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setImageResource(images[position])
    }
}