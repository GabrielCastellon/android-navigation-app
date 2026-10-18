package com.gabriel.gabrielcastellon_comp304lab4_ex1

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


fun createGeofence(
    geofenceId: String,
    latLng: LatLng,
    radius: Float
): Geofence {
    return Geofence.Builder()
        .setRequestId(geofenceId)  // Unique identifier for the geofence
        .setCircularRegion(latLng.latitude, latLng.longitude, radius) // Define geofence region
        .setExpirationDuration(Geofence.NEVER_EXPIRE) // Geofence does not expire
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT) // Handle both entry and exit transitions
        .build()
}

// Build the geofencing request to manage geofencing behavior
fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
    return GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
}

// Add a geofence to the system
fun addGeofence(context: Context, geofence: Geofence) {
    val geofencingClient = LocationServices.getGeofencingClient(context)
    val geofencingRequest = buildGeofencingRequest(geofence)

    // Create a pending intent to handle geofence transitions
    val geofencePendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, GeofenceBroadcastReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )

    // Check for location permissions before adding geofence
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    // Add the geofence and handle success or failure
    geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
        .addOnSuccessListener {
            Toast.makeText(context, "Geofence added", Toast.LENGTH_LONG).show()
            Log.d("app","Geofence added" )
        }
        .addOnFailureListener {

            Toast.makeText(context, "Failed to add geofence", Toast.LENGTH_LONG).show()
        }
}
