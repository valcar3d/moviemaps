package com.example.moviemapsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviemapsapp.db.entities.MovieDetailsEntity
import com.example.moviemapsapp.db.entities.MovieResultItemEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieItem(movieItem: MutableList<MovieResultItemEntity>)

    @Query("SELECT * FROM movieresultitementity")
    suspend fun getMovieItems(): List<MovieResultItemEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieItemDetail(movieDetailItem: MovieDetailsEntity)


    @Query("SELECT * FROM moviedetailsentity")
    suspend fun getMovieItemsDetails(): List<MovieDetailsEntity>


    @Query("SELECT * FROM moviedetailsentity WHERE id=:id")
    fun getSingleMovieDetails(id: Int): MovieDetailsEntity

}