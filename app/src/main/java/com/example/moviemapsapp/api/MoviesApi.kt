package com.example.moviemapsapp.api


import com.example.moviemapsapp.BuildConfig
import com.example.moviemapsapp.models.MoviesListModel
import com.example.moviemapsapp.models.moviedetailsmodel.MovieDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {
    @GET("discover/movie?api_key=${BuildConfig.API_KEY}&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_watch_monetization_types=flatrate")
    fun getPopularMovies(): Call<MoviesListModel>

    @GET("https://api.themoviedb.org/3/movie/{movieId}?api_key=${BuildConfig.API_KEY}&language=en-US")
    fun getMovieDetail(@Path("movieId") movieId: String): Call<MovieDetails>
}