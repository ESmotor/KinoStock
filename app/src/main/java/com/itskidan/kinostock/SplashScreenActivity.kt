package com.itskidan.kinostock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.itskidan.kinostock.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Добавьте задержку, если нужно
        Handler(Looper.getMainLooper()).postDelayed({
            // Здесь можно запустить следующую активность
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2000 миллисекунд (2 секунды)
    }
}