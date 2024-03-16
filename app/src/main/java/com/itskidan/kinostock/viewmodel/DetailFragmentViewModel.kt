package com.itskidan.kinostock.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.itskidan.core_api.entity.Film
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailFragmentViewModel : ViewModel() {


    suspend fun loadWallpaper(url: String): Bitmap {
        return suspendCoroutine {
            val url = URL(url)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            it.resume(bitmap)
        }
    }

    fun onShareClick(context: Context, film: Film) {
        //Create an intent
        val intent = Intent()
        //Specify the action with which it is launched
        intent.action = Intent.ACTION_SEND
        //Put data about our movie
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Check out this film: ${film.title} \n\n ${film.description}"
        )
        //Specify the MIME type so that the system knows which application to offer
        intent.type = "text/plain"
        //Launch our activity
        context.startActivity(Intent.createChooser(intent, "Share To:"))
    }
}