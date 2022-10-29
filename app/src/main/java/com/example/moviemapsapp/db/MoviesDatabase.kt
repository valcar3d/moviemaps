package com.example.moviemapsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviemapsapp.db.entities.MovieDetailsEntity
import com.example.moviemapsapp.db.entities.MovieResultItemEntity

@Database(entities = [MovieResultItemEntity::class, MovieDetailsEntity::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val dao: MovieDao
}