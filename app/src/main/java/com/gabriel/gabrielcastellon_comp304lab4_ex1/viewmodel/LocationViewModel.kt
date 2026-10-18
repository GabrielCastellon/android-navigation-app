package com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.Location
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.GeocodeResponse
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gabriel.gabrielcastellon_comp304lab4_ex1.GeocodeWorker
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.Place
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.Step

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    // LiveData for Locations
    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> get() = _locations

    // LiveData for Geocode Result
    private val _geocodeResult = MutableLiveData<GeocodeResponse?>()
    val geocodeResult: LiveData<GeocodeResponse?> get() = _geocodeResult

    // LiveData for Nearby Places
    private val _nearbyPlaces = MutableLiveData<List<Place>>()
    val nearbyPlaces: LiveData<List<Place>> get() = _nearbyPlaces

    // LiveData for Route Steps
    private val _routeSteps = MutableLiveData<List<Step>>()
    val routeSteps: LiveData<List<Step>> get() = _routeSteps

    // Fetch Route between origin and destination
    fun fetchRoute(origin: String, destination: String) {
        viewModelScope.launch {
            val routeResponse = repository.fetchRoute(origin, destination)
            if (routeResponse != null && routeResponse.routes.isNotEmpty()) {
                val steps = routeResponse.routes[0].legs.flatMap { it.steps }
                _routeSteps.postValue(steps) // Update LiveData
            } else {
                Log.e("fetchRoute", "No route found or an error occurred.")
            }
        }
    }

    // Clear all saved locations
    fun clearAllSavedLocations() {
        viewModelScope.launch {
            repository.clearLocations()
            _locations.postValue(emptyList()) // Clear LiveData
            Log.d("clearLocations", "All locations cleared.")
        }
    }

    // Fetch Nearby Places
    fun fetchNearbyPlaces(location: String, radius: Int) {
        viewModelScope.launch {
            val placesResponse = repository.fetchNearbyPlaces(location, radius)
            if (placesResponse != null && placesResponse.status == "OK") {
                _nearbyPlaces.postValue(placesResponse.results)
            } else {
                Log.e("fetchNearbyPlaces", "Error fetching nearby places or no results.")
            }
        }
    }

    // Fetch Geocode Address (used in GeocodeScreen)
    fun fetchGeocode(latlng: String) {
        viewModelScope.launch {
            val geocode = repository.fetchGeocode(latlng)
            if (geocode != null) {
                _geocodeResult.postValue(geocode)
            } else {
                _geocodeResult.postValue(GeocodeResponse(emptyList(), "No address found"))
            }
        }
    }


    // Save a new location to the database
    fun saveLocation(location: Location) {
        viewModelScope.launch {
            repository.insertLocation(location)
        }
    }

    // Retrieve all saved locations from the database
    fun getSavedLocations() {
        viewModelScope.launch {
            val savedLocations = repository.getAllLocations()
            _locations.postValue(savedLocations)
        }
    }

    // Schedule Geocode Work using WorkManager
    fun scheduleGeocodeWork(context: Context, latLng: String) {
        val inputData = Data.Builder()
            .putString("latLng", latLng)
            .build()

        val geocodeWorkRequest = OneTimeWorkRequestBuilder<GeocodeWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(geocodeWorkRequest)
    }

}
