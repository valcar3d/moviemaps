package com.example.moviemapsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemapsapp.R
import com.example.moviemapsapp.interfaces.OnMovieClickedListener
import com.example.moviemapsapp.models.MovieResultItem
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesListAdapter(var movies: List<MovieResultItem>, private val listener: OnMovieClickedListener) :
    RecyclerView.Adapter<MoviesListAdapter.MoviesViewHolder>() {

    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position : Int = bindingAdapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onMovieClicked(movies[position].id)
            }
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesListAdapter.MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(movieItem: MoviesListAdapter.MoviesViewHolder, position: Int) {
        movieItem.itemView.apply {
            tvTitulo.text = "Titulo: \n${movies[position].title}"
            tvSinopsis.text = "Sinopsis: \n${movies[position].overview}"
            tvEstreno.text = "Estreno: \n${movies[position].releaseDate}"
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}