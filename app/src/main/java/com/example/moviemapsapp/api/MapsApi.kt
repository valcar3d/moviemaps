package com.example.moviemapsapp.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapsApi {
    @GET("directions/json?&key=AIzaSyAHFPYvCdQm3D29QCAKLIQkZll4aTRixnk")
    fun getRouteCalculation(
        @Query("origin") originCoordinates: String,
        @Query("destination") destination: String
    ): Call<ResponseBody>
}