package com.example.moviemapsapp.models.moviedetailsmodel

import com.google.gson.annotations.SerializedName

data class ProductionCompaniesItem(@SerializedName("logo_path")
                                   val logoPath: String? = null,
                                   @SerializedName("name")
                                   val name: String = "",
                                   @SerializedName("id")
                                   val id: Int = 0,
                                   @SerializedName("origin_country")
                                   val originCountry: String = "")