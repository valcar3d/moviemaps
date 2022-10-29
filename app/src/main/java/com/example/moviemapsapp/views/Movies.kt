package com.example.moviemapsapp.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moviemapsapp.adapters.MoviesListAdapter
import com.example.moviemapsapp.api.MoviesApi
import com.example.moviemapsapp.databinding.ActivityMoviesBinding
import com.example.moviemapsapp.db.MoviesDatabase
import com.example.moviemapsapp.db.entities.MovieResultItemEntity
import com.example.moviemapsapp.di.AppComponent
import com.example.moviemapsapp.interfaces.OnMovieClickedListener
import com.example.moviemapsapp.models.MovieResultItem
import com.example.moviemapsapp.models.MoviesListModel
import com.example.moviemapsapp.utilities.Utilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Movies : AppCompatActivity(), OnMovieClickedListener {
    private val TAG: String = Movies::class.java.simpleName
    private lateinit var binding: ActivityMoviesBinding
    @Inject
    lateinit var mMoviesApiService: MoviesApi

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init dagger
        (application as AppComponent).getRetroComponent().inject(this)

        binding = ActivityMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //init database
        val moviesDatabase = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java,
            "movies_database.db"
        ).build()

        //If no internet connection use the saved database
        if (!Utilities.isOnline(this)) {
            Toast.makeText(this, "Sin Conexion", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO) {
                val savedMovies = moviesDatabase.dao.getMovieItems()
                val convertedSavedMovies: MutableList<MovieResultItem> = mutableListOf()

                if (savedMovies.isEmpty()) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@Movies, "No hay peliculas guardadas", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    //get saved values in the database
                    for (i in 0 until savedMovies.size) {
                        convertedSavedMovies.add(
                            MovieResultItem(
                                id = savedMovies[i].id,
                                title = savedMovies[i].title,
                                releaseDate = savedMovies[i].releaseDate,
                                overview = savedMovies[i].overview,
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        val adapter = MoviesListAdapter(convertedSavedMovies, this@Movies)
                        binding.rvMovies.adapter = adapter
                        binding.rvMovies.layoutManager = LinearLayoutManager(applicationContext)
                    }
                }
            }
        } else {
            Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show()
            //get movie overview
            lifecycleScope.launch {
                val getMoviesList = mMoviesApiService.getPopularMovies()
                getMoviesList.enqueue(object : Callback<MoviesListModel> {
                    override fun onResponse(
                        call: Call<MoviesListModel>,
                        response: Response<MoviesListModel>
                    ) {
                        if (response.code() == 200) {
                            val listOfMovies = response.body()?.results
                            val moviesForDataBase: MutableList<MovieResultItemEntity> =
                                mutableListOf()

                            //get values for the database movies
                            for (i in 0 until listOfMovies!!.size) {
                                moviesForDataBase.add(
                                    MovieResultItemEntity(
                                        id = listOfMovies[i].id,
                                        title = listOfMovies[i].title,
                                        releaseDate = listOfMovies[i].releaseDate,
                                        overview = listOfMovies[i].overview
                                    )
                                )
                            }

                            //insert fetched movies into database
                            lifecycleScope.launch(Dispatchers.IO) {
                                moviesDatabase.dao.insertMovieItem(moviesForDataBase)
                            }

                            val adapter = MoviesListAdapter(listOfMovies, this@Movies)
                            binding.rvMovies.adapter = adapter
                            binding.rvMovies.layoutManager = LinearLayoutManager(applicationContext)

                        } else {
                            Log.d(TAG, "onResponse: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<MoviesListModel>, t: Throwable) {
                        Log.d(TAG, "onFailure: ")
                    }

                })

            }
        }


    }

    override fun onMovieClicked(movieId: Int) {
        val intent = Intent(this, MoviesDetailsView::class.java)
        intent.putExtra("movieId", movieId)
        startActivity(intent)
    }
}