package com.gabriel.gabrielcastellon_comp304lab4_ex1.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationViewModel

@Composable
fun MapScreen(locationViewModel: LocationViewModel) {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            LatLng(43.651070, -79.347015), 10f // Default to Toronto
        )
    }
    val savedLocations by locationViewModel.locations.observeAsState(emptyList())
    val userLocation = remember { mutableStateOf<LatLng?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    userLocation.value?.let { latLng ->
                        locationViewModel.saveLocation(
                            Location(
                                id = 0, // Use `0` for auto-generated IDs
                                name = "User Marker",
                                latitude = latLng.latitude,
                                longitude = latLng.longitude,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    }
                },
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Place, contentDescription = "Save Location")
            }
        },
        content = { paddingValues ->
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Apply padding
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                // Render saved locations
                savedLocations.forEach { location ->
                    Marker(
                        state = rememberMarkerState(
                            position = LatLng(location.latitude, location.longitude)
                        ),
                        title = location.name,
                        snippet = "Saved Location"
                    )
                }

                // Render user location if available
                userLocation.value?.let { location ->
                    Marker(
                        state = rememberMarkerState(position = location),
                        title = "Your Location",
                        snippet = "You're here!"
                    )
                }
            }
        }
    )

}

