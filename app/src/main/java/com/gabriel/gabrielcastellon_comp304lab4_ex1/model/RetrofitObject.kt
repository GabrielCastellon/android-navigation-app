package com.gabriel.gabrielcastellon_comp304lab4_ex1.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    // Google Maps API for geocoding, directions, and places
    val googleMapsApi: AppLocationApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AppLocationApi::class.java)
    }
}
