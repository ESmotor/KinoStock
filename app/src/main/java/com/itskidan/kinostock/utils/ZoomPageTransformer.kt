package com.itskidan.kinostock.utils

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

const val MIN_SCALE = 0.97f
const val MIN_ALPHA = 0.8f

class ZoomPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // Эта страница уехала за левый край
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    //Обновляем положение  страницы в пространстве
                    val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                    val yMargin = pageHeight * (1 - scaleFactor) / 2
                    val xMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        xMargin - yMargin / 2
                    } else {
                        xMargin + yMargin / 2
                    }

                    //Обновляем размеры
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    //Обновляем прозрачность
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // Эта страница уехала за правый край
                    alpha = 0f
                }
            }
        }
    }
}