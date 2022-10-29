package com.example.moviemapsapp.views

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.moviemapsapp.R
import com.example.moviemapsapp.api.MoviesApi
import com.example.moviemapsapp.databinding.ActivityMoviesDetailsViewBinding
import com.example.moviemapsapp.db.MoviesDatabase
import com.example.moviemapsapp.db.entities.MovieDetailsEntity
import com.example.moviemapsapp.di.AppComponent
import com.example.moviemapsapp.models.moviedetailsmodel.MovieDetails
import com.example.moviemapsapp.utilities.Utilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MoviesDetailsView : AppCompatActivity() {

    private val TAG: String = MoviesDetailsView::class.java.simpleName

    @Inject
    lateinit var mMoviesDetailsApiService: MoviesApi
    private lateinit var binding: ActivityMoviesDetailsViewBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init dagger
        (application as AppComponent).getMovieComponent().inject(this)

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivityMoviesDetailsViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //get movie ID to request for details
        val mIntent = intent
        val movieId = mIntent.getIntExtra("movieId", 0)

        //init database
        val moviesDatabase = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java,
            "movies_database.db"
        ).build()


        if (!Utilities.isOnline(this)) {
            Toast.makeText(this, "Sin Conexion...", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO) {
                val savedMovieDetails = moviesDatabase.dao.getMovieItemsDetails()
                var singleMovieItemDetail = MovieDetailsEntity()

                if (savedMovieDetails.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MoviesDetailsView,
                            "No hay peliculas guardadas",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                } else {
                    singleMovieItemDetail = moviesDatabase.dao.getSingleMovieDetails(movieId)

                    if (singleMovieItemDetail == null) {
                        withContext(Dispatchers.Main) {

                            Toast.makeText(
                                this@MoviesDetailsView,
                                "No hay registro guardado de esta pelicula",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            val intent = Intent(this@MoviesDetailsView, Movies::class.java)
                            startActivity(intent)
                        }

                    }


                    //set saved values in the UI
                    withContext(Dispatchers.Main) {
                        binding.tvTitulo.text =
                            "Titulo Original: ${singleMovieItemDetail?.title}"
                        binding.tvSinopsisCompleta.text =
                            "Sinopsis: \n${singleMovieItemDetail?.overview}"
                        binding.tvFechaEstreno.text =
                            "Estreno: ${singleMovieItemDetail?.releaseDate}"
                        binding.tvGanancias.text =
                            "Recaudacion: ${
                                String.format(
                                    "%,d",
                                    singleMovieItemDetail?.revenue
                                )
                            } dls"
                        binding.tvWebPage.text = "Pagina Web: ${singleMovieItemDetail?.homepage}"

                        val media =
                            "http://image.tmdb.org/t/p/w500/9Rj8l6gElLpRL7Kj17iZhrT5Zuw.jpg"
                        Glide.with(this@MoviesDetailsView)
                            .asBitmap()
                            .load(media)
                            .fitCenter()
                            .placeholder(R.drawable.movie_pattern)
                            .into(binding.imageView)
                    }
                }
            }
        } else {
            //Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show()
            //get movie details
            lifecycleScope.launch {
                //val getMovieDetail = RetrofitHelper.api.getMovieDetail(movieId.toString())
                val getMovieDetail = mMoviesDetailsApiService.getMovieDetail(movieId.toString())
                getMovieDetail.enqueue(object : Callback<MovieDetails> {
                    override fun onResponse(
                        call: Call<MovieDetails>,
                        response: Response<MovieDetails>
                    ) {
                        if (response.code() == 200) {

                            response.body().let { movie ->
                                binding.tvTitulo.text =
                                    "Titulo Original: ${movie?.title.toString()}"
                                binding.tvSinopsisCompleta.text =
                                    "Sinopsis: \n${movie?.overview.toString()}"
                                binding.tvFechaEstreno.text =
                                    "Estreno: ${movie?.releaseDate.toString()}"
                                binding.tvGanancias.text =
                                    "Recaudacion: ${String.format("%,d", movie?.revenue)} dls"
                                binding.tvWebPage.text = "Pagina Web: ${movie?.homepage}"

                                val media =
                                    "http://image.tmdb.org/t/p/w500/9Rj8l6gElLpRL7Kj17iZhrT5Zuw.jpg"
                                Glide.with(this@MoviesDetailsView)
                                    .asBitmap()
                                    .load(media)
                                    .fitCenter()
                                    .placeholder(R.drawable.movie_pattern)
                                    .into(binding.imageView)


                                val movieDetailForDatabase = MovieDetailsEntity()

                                //set values to save the movie details to the database
                                movieDetailForDatabase.apply {
                                    id = movie!!.id
                                    title = movie.title.toString()
                                    overview = movie.overview.toString()
                                    releaseDate = movie.releaseDate.toString()
                                    homepage = movie.homepage.toString()
                                }


                                //insert fetched movies details into database
                                lifecycleScope.launch(Dispatchers.IO) {
                                    moviesDatabase.dao.insertMovieItemDetail(movieDetailForDatabase)
                                }
                            }


                        }
                    }

                    override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "No se pudo cargar: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
            }
        }


    }
}