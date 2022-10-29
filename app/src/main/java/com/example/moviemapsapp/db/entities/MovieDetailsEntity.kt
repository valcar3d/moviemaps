package com.example.moviemapsapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MovieDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var video: Boolean = false,
    var title: String = "",
    var backdropPath: String = "",
    var revenue: Int = 0,
    var popularity: Double = 0.0,
    var budget: Int = 0,
    var overview: String = "",
    var originalTitle: String = "",
    var runtime: Int = 0,
    var posterPath: String = "",
    var releaseDate: String = "",
    var homepage: String = "",
)