package com.example.moviemapsapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.moviemapsapp.R
import com.example.moviemapsapp.databinding.ActivityMainBinding
import com.example.moviemapsapp.databinding.ActivityMapsBinding
import com.example.moviemapsapp.databinding.ActivityTravelDataViewBinding
import com.example.moviemapsapp.viewmodels.CoordinatesViewModel

class TravelDataView : AppCompatActivity() {
    private lateinit var binding: ActivityTravelDataViewBinding
    private lateinit var timeToArrive: String
    private lateinit var distance: String
    private lateinit var originCoordinates: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTravelDataViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //get movie ID to request for details
        val mIntent = intent
        timeToArrive = mIntent.getStringExtra("timeToArrive").toString()
        distance = mIntent.getStringExtra("totalDistance").toString()
        originCoordinates = mIntent.getStringExtra("originCoordinates").toString()

        binding.tvCoordinates.text = "En Auto desde el origen:\n $originCoordinates"
        binding.tvTiempo.text = "Tiempo para llegar: $timeToArrive"
        binding.tvDistancia.text = "Distancia Total: $distance"
    }
}