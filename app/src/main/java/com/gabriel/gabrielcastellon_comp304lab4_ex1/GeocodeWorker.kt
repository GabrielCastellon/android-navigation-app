package com.gabriel.gabrielcastellon_comp304lab4_ex1

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log

class GeocodeWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Retrieve the latitude and longitude string from the input data
        val latLng = inputData.getString("latLng")
        return try {
            // Log and perform geocoding logic here
            Log.d("GeocodeWorker", "Geocoding for location: $latLng")
            Result.success()
        } catch (e: Exception) {
            // Log the error if geocoding fails
            Log.e("GeocodeWorker", "Error during geocoding", e)
            Result.failure()
        }
    }
}
