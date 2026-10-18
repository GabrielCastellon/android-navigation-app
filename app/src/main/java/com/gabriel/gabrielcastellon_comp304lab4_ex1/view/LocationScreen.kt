package com.gabriel.gabrielcastellon_comp304lab4_ex1.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.Location
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(locationViewModel: LocationViewModel, navController: NavController) {
    val savedLocations by locationViewModel.locations.observeAsState(emptyList())

    // Fetch saved locations
    LaunchedEffect(Unit) {
        locationViewModel.getSavedLocations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Locations") },
                actions = {
                    IconButton(onClick = { locationViewModel.clearAllSavedLocations() }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Clear All")
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (savedLocations.isEmpty()) {
                    item { Text("No saved locations.") }
                } else {
                    items(savedLocations) { location ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = location.name, style = MaterialTheme.typography.bodyLarge)
                                Text(
                                    text = "Lat: ${location.latitude}, Lng: ${location.longitude}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun NearbyPlacesScreen(locationViewModel: LocationViewModel) {
    var location by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("1000") }
    val nearbyPlaces by locationViewModel.nearbyPlaces.observeAsState(emptyList())

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Enter Location (e.g., 37.7749,-122.4194)") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = radius,
            onValueChange = { radius = it },
            label = { Text("Enter Radius (meters)") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val radiusInt = radius.toIntOrNull()
            if (radiusInt != null && location.isNotBlank()) {
                locationViewModel.fetchNearbyPlaces(location, radiusInt)
            } else {
                // Show error (Toast or message)
            }
        }) {
            Text("Search Nearby")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            if (nearbyPlaces.isEmpty()) {
                item { Text("No nearby places found.") }
            } else {
                items(nearbyPlaces) { place ->
                    Text("${place.name} - ${place.vicinity}")
                }
            }
        }
    }
}



@Composable
fun SaveLocationScreen(locationViewModel: LocationViewModel) {
    var name by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    // Access the context at the start of the composable
    val context = LocalContext.current

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val lat = latitude.toDoubleOrNull()
            val lng = longitude.toDoubleOrNull()
            val timestamp = System.currentTimeMillis()

            if (lat != null && lng != null && name.isNotBlank()) {
                locationViewModel.saveLocation(
                    Location(name = name, latitude = lat, longitude = lng, timestamp = timestamp)
                )
                // Show success toast
                Toast.makeText(context, "Location saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                // Show error toast
                Toast.makeText(context, "Invalid input. Please check all fields.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save")
        }
    }
}


@Composable
fun SavedLocationsScreen(locationViewModel: LocationViewModel) {
    val savedLocations by locationViewModel.locations.observeAsState(emptyList())

    LazyColumn(Modifier.padding(16.dp)) {
        if (savedLocations.isEmpty()) {
            item { Text("No saved locations.") }
        } else {
            items(savedLocations) { location ->
                Text("Name: ${location.name}, Lat: ${location.latitude}, Lng: ${location.longitude}")
            }
        }
    }
}




