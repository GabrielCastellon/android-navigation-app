package com.gabriel.gabrielcastellon_comp304lab4_ex1.view

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.LatLng
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationViewModel
import com.google.android.gms.location.LocationServices
const val LOCATION_PERMISSION_REQUEST_CODE = 1001


@Composable
fun RoutePlannerScreen(locationViewModel: LocationViewModel, navController: NavController, searchQuery: String) {
    // Origin defaults to "Your Location" or fetched location
    var origin by remember { mutableStateOf("Your Location") }
    var destination by remember { mutableStateOf(searchQuery) }

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Request location permission if needed
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request location permission
        ActivityCompat.requestPermissions(
            (context as Activity),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    // Fetch current location for origin
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    origin = "${location.latitude},${location.longitude}"
                } else {
                    // Fallback if location is null
                    origin = "43.651070,-79.347015" // Default to Toronto
                }
            }.addOnFailureListener {
                // Handle failure
                origin = "43.651070,-79.347015" // Default to Toronto
            }
        } else {
            // Permission not granted, default to Toronto
            origin = "43.651070,-79.347015"
        }
    }


    Column(Modifier.padding(16.dp)) {
        // Origin field (non-editable)
        OutlinedTextField(
            value = origin,
            onValueChange = {},
            label = { Text("From Origin") },
            enabled = false // Disable editing for origin
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Destination field
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("To Destination") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Fetch Route Button
        Button(onClick = { locationViewModel.fetchRoute(origin, destination) }) {
            Text("Fetch Route")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Display route steps
        val routeSteps by locationViewModel.routeSteps.observeAsState(emptyList())
        LazyColumn {
            items(routeSteps) { step ->
                val plainText = step.htmlInstructions.replace(Regex("<[^>]*>"), "")
                Text(text = "- $plainText")
            }
        }
    }
}
