package com.example.moviemapsapp.di

import android.app.Application
import com.example.moviemapsapp.di.components.*
import com.example.moviemapsapp.di.modules.GoogleMapsModule
import com.example.moviemapsapp.di.modules.MoviesDetailsModule
import com.example.moviemapsapp.di.modules.MoviesRetrofitModule

class AppComponent : Application() {
    private lateinit var retroComponent: RetroComponent
    private lateinit var moviesComponent: MoviesDetailsComponent
    private lateinit var googleMapsComponent: GoogleMapsComponent

    override fun onCreate() {
        super.onCreate()
        retroComponent = DaggerRetroComponent.builder()
            .moviesRetrofitModule(MoviesRetrofitModule())
            .build()
        moviesComponent = DaggerMoviesDetailsComponent.builder()
            .moviesDetailsModule(MoviesDetailsModule())
            .build()

        googleMapsComponent = DaggerGoogleMapsComponent.builder()
            .googleMapsModule(GoogleMapsModule())
            .build()
    }

    fun getRetroComponent(): RetroComponent {
        return retroComponent
    }

    fun getMovieComponent(): MoviesDetailsComponent {
        return moviesComponent
    }

    fun getMapsComponent(): GoogleMapsComponent {
        return googleMapsComponent
    }
}