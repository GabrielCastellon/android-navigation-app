package com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val locationRepository: LocationRepository) : ViewModelProvider.Factory {

    // Creates and returns the right ViewModel instance based on class type.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Check if the requested ViewModel is LocationViewModel
            modelClass.isAssignableFrom(LocationViewModel::class.java) -> {
                LocationViewModel(locationRepository) as T
            }
            // Throw an exception for unknown ViewModel classes
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
