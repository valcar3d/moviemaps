package com.example.moviemapsapp.di.components

import com.example.moviemapsapp.di.modules.MoviesRetrofitModule
import com.example.moviemapsapp.views.Movies
import dagger.Component
import javax.inject.Singleton

@Component(modules = [MoviesRetrofitModule::class])
@Singleton
interface RetroComponent {
    fun inject(moviesOverview: Movies)
}