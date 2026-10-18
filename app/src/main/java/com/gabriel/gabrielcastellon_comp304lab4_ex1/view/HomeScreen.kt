package com.gabriel.gabrielcastellon_comp304lab4_ex1.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager // Add this
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.AppLocationApi
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.GeocodeResponse
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.Location
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.RetrofitObject
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationViewModel
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, context: Context, locationViewModel: LocationViewModel) {
    // Map state to manage camera position
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            LatLng(43.651070, -79.347015), 10f // Default to Toronto
        )
    }
    // State for search query and user location
    val searchQuery = remember { mutableStateOf("") }
    val userLocation = remember { mutableStateOf<LatLng?>(null) }

    // State for markers and geofences
    val markers = remember { mutableStateListOf<LatLng>() }
    val geofences = remember { mutableStateListOf<Pair<LatLng, Float>>() }


    Scaffold(
        topBar = {
            // Search bar at the top
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        label = { Text("Search Location") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.9f), // Slightly transparent
                                shape = RoundedCornerShape(2.dp)
                            ),
                        trailingIcon = {
                            // Navigate to RoutePlannerScreen with the search query
                            IconButton(onClick = {
                                navController.navigate("routePlanner/${searchQuery.value}") }) {
                                Icon(Icons.Filled.Search, contentDescription = "Search")
                            }
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                // Button 1: Add Marker and Geofence
                FloatingActionButton(
                    onClick = {
                        val latLng = cameraPositionState.position.target
                        markers.add(latLng)
                        geofences.add(Pair(latLng, 1000f)) // Add geofence with 1km radius
                        userLocation.value = latLng // Update the user location
                        cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(latLng, 12f)
                        Toast.makeText(context, "Marker and Geofence added!", Toast.LENGTH_SHORT).show()
                    },
                    containerColor = Color.White, // Purple
                    contentColor = Color(0xFFFF4033)
                ) {
                    Icon(Icons.Filled.Place, contentDescription = "Add Marker and Geofence")
                }

                // Button 2: Save Location
                FloatingActionButton(
                    onClick = {
                        userLocation.value?.let { location ->
                            locationViewModel.saveLocation(
                                Location(
                                    name = "Saved Location",
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                            Toast.makeText(context, "Location saved!", Toast.LENGTH_SHORT).show()
                        } ?: run {
                            Toast.makeText(context, "No location to save", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = Color.White,
                    contentColor = Color(0xFFFF4033) //RED
                ) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Save Location")
                }

                // Button 3: Navigate to Routes
                FloatingActionButton(
                    onClick = {
                        val safeSearchQuery = searchQuery.value.ifEmpty { "End location" }
                        navController.navigate("routePlanner/$safeSearchQuery")
                    },
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Go to Routes")
                }

            }
        },
        content = {
            // Display Google Map with markers and geofences
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                // Render markers
                markers.forEach { position ->
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = "Marker",
                        snippet = "User-added Marker"
                    )
                }

                // Render geofences
                geofences.forEach { (center, radius) ->
                    Circle(
                        center = center,
                        radius = radius.toDouble(),
                        fillColor = Color(0x5500FF00).copy(alpha = 0.34f),
                        strokeColor = Color(0xFF00FF00),
                        strokeWidth = 2f
                    )
                }
            }
        }
    )
}
