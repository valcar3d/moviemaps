package com.example.moviemapsapp.di.components

import com.example.moviemapsapp.di.modules.MoviesDetailsModule
import com.example.moviemapsapp.views.MoviesDetailsView
import dagger.Component
import javax.inject.Singleton

@Component(modules = [MoviesDetailsModule::class])
@Singleton
interface MoviesDetailsComponent {
    fun inject(moviesDetailsComponent: MoviesDetailsView)
}