package com.gabriel.gabrielcastellon_comp304lab4_ex1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the intent has an error and handle it
        if (GeofencingEvent.fromIntent(intent)?.hasError() == true) {
            val errorCode = GeofencingEvent.fromIntent(intent)?.errorCode
            Log.e("GeofenceBroadcastReceiver", "Geofence error occurred: $errorCode")
            Toast.makeText(context, "Geofence error occurred: $errorCode", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve geofencing event and the transition type (e.g., enter/exit)
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val transition = geofencingEvent?.geofenceTransition ?: return


        // Handle geofence transitions (enter or exit)
        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("app","Entered geofence --- You Are Close To Costco")
            Toast.makeText(context, "Entered geofence", Toast.LENGTH_SHORT).show()
        } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d("app","Exited geofence")
            Toast.makeText(context, "Exited geofence - I don't Care", Toast.LENGTH_SHORT).show()
        }
    }
}