package com.example.moviemapsapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieResultItemEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val overview: String = "",
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val video: Boolean = false,
    val title: String = "",
    val posterPath: String = "",
    val backdropPath: String = "",
    val releaseDate: String = "",
    val popularity: Double = 0.0,
    val voteAverage: Double = 0.0,
    val adult: Boolean = false,
    val voteCount: Int = 0
)