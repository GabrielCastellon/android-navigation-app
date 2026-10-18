package com.gabriel.gabrielcastellon_comp304lab4_ex1.view

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationViewModel

@Composable
fun GeocodeScreen(locationViewModel: LocationViewModel, context: Context) {
    // State to hold the user-inputted latitude and longitude
    var latLng by remember { mutableStateOf("") }

    // Observing geocode results from ViewModel
    val geocodeResult by locationViewModel.geocodeResult.observeAsState()

    Column(Modifier.padding(16.dp)) {
        // Text field to input latitude and longitude
        OutlinedTextField(
            value = latLng,
            onValueChange = { latLng = it },
            label = { Text("Enter LatLng (e.g., 37.7749,-122.4194)") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Button to fetch address based on input LatLng
        Button(onClick = { locationViewModel.fetchGeocode(latLng) }) {
            Text("Fetch Address")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Display the geocoded address if available
        geocodeResult?.let { result ->
            if (result.results.isNotEmpty()) {
                Text("Address: ${result.results[0].formattedAddress}")
            } else {
                Text("No address found.")
            }
        }
    }
}

