package com.gabriel.gabrielcastellon_comp304lab4_ex1.model

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface AppLocationApi {
    // Google Maps Geocoding API: Convert lat/lng to address
    @GET("geocode/json")
    suspend fun getGeocode(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): Response<GeocodeResponse>

    // Google Maps Directions API: Fetch route between two locations
    @GET("directions/json")
    suspend fun getRoute(
        @Query("origin") origin: String, // "latitude,longitude"
        @Query("destination") destination: String, // "latitude,longitude"
        @Query("key") apiKey: String
    ): Response<RouteResponse>

    // Google Places API (Optional): Get nearby places (e.g., for recommendations)
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String, // "latitude,longitude"
        @Query("radius") radius: Int, // Radius in meters
        @Query("key") apiKey: String
    ): Response<NearbyPlacesResponse>
}

