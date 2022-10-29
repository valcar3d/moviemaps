package com.example.moviemapsapp.models.moviedetailsmodel

import com.google.gson.annotations.SerializedName

data class BelongsToCollection(@SerializedName("backdrop_path")
                               val backdropPath: String = "",
                               @SerializedName("name")
                               val name: String = "",
                               @SerializedName("id")
                               val id: Int = 0,
                               @SerializedName("poster_path")
                               val posterPath: String = "")