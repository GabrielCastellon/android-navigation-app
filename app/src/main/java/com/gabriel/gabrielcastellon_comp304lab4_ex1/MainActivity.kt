package com.gabriel.gabrielcastellon_comp304lab4_ex1

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gabriel.gabrielcastellon_comp304lab4_ex1.model.LocationDatabase
import com.gabriel.gabrielcastellon_comp304lab4_ex1.ui.theme.Gabrielcastellon_COMP304Lab4_Ex1Theme
import com.gabriel.gabrielcastellon_comp304lab4_ex1.view.*
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationRepository
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.ViewModelFactory
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {

    // Launcher for requesting location permissions
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if permissions are granted
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val backgroundLocationGranted =
            permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] ?: false

        if (fineLocationGranted && (backgroundLocationGranted || android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q)) {
            setupGeofence() // Set up geofence after permissions are granted
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request permissions
        requestLocationPermissions()

        // Initialize database, repository, and ViewModel factory
        val database = LocationDatabase.getInstance(this)
        val locationDao = database.getLocationDao()
        val repository = LocationRepository(locationDao)
        val factory = ViewModelFactory(repository)

        setContent {
            // Set the app's theme and navigation structure
            Gabrielcastellon_COMP304Lab4_Ex1Theme {
                AppNavigation(factory = factory)
            }
        }
    }

    // Request location permissions
    private fun requestLocationPermissions() {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        // Launch permission request
        locationPermissionLauncher.launch(permissions.toTypedArray())
    }

    // Set up geofence
    private fun setupGeofence() {
        val geofence = createGeofence(
            geofenceId = "CostcoGeofence",
            latLng = LatLng(43.651070, -79.347015), // Replace with your geofence location
            radius = 100f // Radius in meters
        )
        addGeofence(this, geofence)
    }
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        // Navigation item: Home
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false, // Update based on state
            onClick = { navController.navigate("home") }
        )
        // Navigation item: Locations
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Locations") },
            label = { Text("Locations") },
            selected = false,
            onClick = {
                navController.navigate("locations")
            }
        )
        // Navigation item: Geocode
        NavigationBarItem(
            icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Geocode") },
            label = { Text("Geocode") },
            selected = false,
            onClick = { navController.navigate("geocode") }
        )
        // Navigation item: Settings
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { navController.navigate("settings") }
        )
    }
}

@Composable
fun AppNavigation(factory: ViewModelFactory) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // Add navigation bar at the bottom
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home", // Default start screen
            modifier = Modifier.padding(innerPadding)
        ) {
            // Navigation routes and their respective screens
            composable("home") {
                HomeScreen(navController = navController, context = LocalContext.current, locationViewModel = viewModel(factory = factory))
            }
            composable("locations") {
                LocationScreen(
                    locationViewModel = viewModel(factory = factory),
                    navController = navController
                )
            }
            composable("map") {
                MapScreen(locationViewModel = viewModel(factory = factory))
            }
            composable(
                route = "routePlanner/{searchQuery}",
                arguments = listOf(navArgument("searchQuery") { defaultValue = "" })
            ) { backStackEntry ->
                val searchQuery = backStackEntry.arguments?.getString("searchQuery") ?: ""
                RoutePlannerScreen(
                    locationViewModel = viewModel(factory = factory),
                    navController = navController,
                    searchQuery = searchQuery
                )
            }

            composable("settings") {
                SettingsScreen(locationViewModel = viewModel(factory = factory))
            }
            composable("geocode") {
                GeocodeScreen(
                    locationViewModel = viewModel(factory = factory),
                    context = LocalContext.current
                )
            }
        }
    }
}

