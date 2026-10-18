package com.gabriel.gabrielcastellon_comp304lab4_ex1.model

import com.google.gson.annotations.SerializedName

// Represents a location retrieved from or sent to an API.
data class ApiLocation(
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timestamp") val timestamp: Long = System.currentTimeMillis()
)

// Response model for a route request.
data class RouteResponse(
    val routes: List<Route>, // List of available routes.
    val status: String // API status (e.g., "OK" or "ERROR").
)

// Represents a route containing legs (segments of the route).
data class Route(
    val legs: List<Leg> // Segments of the route.
)

// Represents a segment (leg) of a route.
data class Leg(
    val distance: Distance,
    val duration: Duration,
    @SerializedName("end_address") val endAddress: String,
    @SerializedName("start_address") val startAddress: String,
    val steps: List<Step> // Steps within the leg.
)

// Represents the distance of a route or step.
data class Distance(
    val text: String, // Human-readable distance (e.g., "5 km").
    val value: Int // Distance in meters.
)

// Represents the duration of a route or step.
data class Duration(
    val text: String, // Human-readable duration (e.g., "10 mins").
    val value: Int // Duration in seconds.
)

// Represents a single step in a route.
data class Step(
    @SerializedName("html_instructions") val htmlInstructions: String, // Step instructions in HTML.
    val distance: Distance,
    val duration: Duration,
    @SerializedName("end_location") val endLocation: LatLng, // End coordinates of the step.
    @SerializedName("start_location") val startLocation: LatLng // Start coordinates of the step.
)

// Represents latitude and longitude coordinates.
data class LatLng(
    val lat: Double,
    val lng: Double
)

// Response model for a geocoding request.
data class GeocodeResponse(
    val results: List<GeocodeResult> = emptyList(), // List of geocoding results.
    val status: String // API status (e.g., "OK" or "ERROR").
)

// Represents a single geocoding result.
data class GeocodeResult(
    @SerializedName("formatted_address") val formattedAddress: String? = null, // Full address.
    val geometry: Geometry // Geometry information for the result.
)

// Geometry data, including location (latitude/longitude).
data class Geometry(
    val location: LatLng // Latitude and longitude of the location.
)

// Response model for a nearby places request.
data class NearbyPlacesResponse(
    val results: List<Place>, // List of nearby places.
    val status: String // API status (e.g., "OK" or "ERROR").
)

// Represents a single place in the nearby places response.
data class Place(
    val name: String,
    val vicinity: String,
    val geometry: Geometry,
    val rating: Float?, // Rating of the place.
    @SerializedName("place_id") val placeId: String // Unique identifier for the place.
)
