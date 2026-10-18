package com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel

import android.util.Log
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.gabriel.gabrielcastellon_comp304lab4_ex1.BuildConfig

class LocationRepository(
    private val locationDao: LocationDao, // Injected DAO for database operations
) {
    private val api = RetrofitObject.googleMapsApi // Retrofit API instance
    private val apiKey = BuildConfig.MAPS_API_KEY

    // Fetch geocode (address) from latitude and longitude
    suspend fun fetchGeocode(latlng: String): GeocodeResponse? {
        return try {
            val response = api.getGeocode(latlng, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Fetch directions between two locations
    suspend fun fetchRoute(origin: String, destination: String): RouteResponse? {
        return try {
            val response = api.getRoute(origin, destination, apiKey)
            Log.d("fetchRoute", "Response: ${response.body()} Code: ${response.code()}")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("fetchRoute", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Fetch nearby places from a given location
    suspend fun fetchNearbyPlaces(location: String, radius: Int): NearbyPlacesResponse? {
        return try {
            val response = api.getNearbyPlaces(location, radius, apiKey)
            Log.d("fetchNearbyPlaces", "Response: ${response.body()} Code: ${response.code()}")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("fetchNearbyPlaces", "Error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    // Insert location data into the database
    suspend fun insertLocation(location: Location) {
        withContext(Dispatchers.IO) {
            locationDao.insert(location)
        }
    }

    // Fetch all saved locations from the database
    suspend fun getAllLocations(): List<Location> {
        return withContext(Dispatchers.IO) {
            locationDao.getAllLocations()
        }
    }

    // Clear all saved locations from the database
    suspend fun clearLocations() {
        withContext(Dispatchers.IO) {
            locationDao.clearAll()
        }
    }
}
