package com.example.moviemapsapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviemapsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //open map view
        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, Maps::class.java)
            startActivity(intent)
        }


        //open movies view
        binding.btnMovies.setOnClickListener {
            val intent = Intent(this, Movies::class.java)
            startActivity(intent)
        }
    }
}