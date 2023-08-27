package com.itskidan.kinostock

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itskidan.kinostock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.button1.setOnClickListener {
            val msg = mainBinding.button1.text.toString()
            showToastMsg(msg)
        }
        mainBinding.button2.setOnClickListener {
            val msg = mainBinding.button2.text.toString()
            showToastMsg(msg)
        }
        mainBinding.button3.setOnClickListener {
            val msg = mainBinding.button3.text.toString()
            showToastMsg(msg)
        }
        mainBinding.button4.setOnClickListener {
            val msg = mainBinding.button4.text.toString()
            showToastMsg(msg)
        }
        mainBinding.button5.setOnClickListener {
            val msg = mainBinding.button5.text.toString()
            showToastMsg(msg)
        }
    }

    private fun showToastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}