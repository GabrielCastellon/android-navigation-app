package com.gabriel.gabrielcastellon_comp304lab4_ex1.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.material3.Switch
import androidx.compose.ui.platform.LocalContext
import com.gabriel.gabrielcastellon_comp304lab4_ex1.addGeofence
import com.gabriel.gabrielcastellon_comp304lab4_ex1.createGeofence
import com.gabriel.gabrielcastellon_comp304lab4_ex1.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun SettingsScreen(locationViewModel: LocationViewModel) {
    // Get the context to use for geofencing operations
    val context = LocalContext.current

    // State to manage geofencing toggle
    var enableGeofencing by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Title for the settings screen
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Row to toggle geofencing
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Enable Geofencing")
            Switch(
                checked = enableGeofencing,
                onCheckedChange = { isChecked ->
                    enableGeofencing = isChecked
                    // Add geofence when enabled
                    if (isChecked) {
                        addGeofence(
                            context = context,
                            geofence = createGeofence(
                                geofenceId = "costco_geofence",
                                latLng = LatLng(37.7749, -122.4194), // Example: San Francisco
                                radius = 100f
                            )
                        )
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}



