package com.example.moviemapsapp.di.components

import com.example.moviemapsapp.di.modules.GoogleMapsModule
import com.example.moviemapsapp.views.Maps
import dagger.Component
import javax.inject.Singleton

@Component(modules = [GoogleMapsModule::class])
@Singleton
interface GoogleMapsComponent {
    fun inject(maps: Maps)
}