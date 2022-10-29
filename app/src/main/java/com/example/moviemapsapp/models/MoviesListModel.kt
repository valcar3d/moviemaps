package com.example.moviemapsapp.models

import com.google.gson.annotations.SerializedName

data class MoviesListModel(@SerializedName("page")
                           val page: Int = 0,
                           @SerializedName("total_pages")
                           val totalPages: Int = 0,
                           @SerializedName("results")
                           val results: List<MovieResultItem>?,
                           @SerializedName("total_results")
                           val totalResults: Int = 0)