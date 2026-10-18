# Android Location Tracker

A native Android application for exploring locations, saving places, viewing directions, and experimenting with location-aware features. Built with **Kotlin**, **Jetpack Compose**, and the **Google Maps SDK**, with a Room database for local persistence.

## Features

- **Interactive map** — Explore a Google Map, adjust the camera, add markers, and visualize circular geofence regions.
- **Saved locations** — Store location names and coordinates locally with Room; browse saved places or clear the list.
- **Route planning** — Enter a destination and request turn-by-turn directions from the Google Directions API, displayed as a list of steps.
- **Reverse geocoding** — Look up a formatted address from latitude and longitude coordinates.
- **Geofencing** — Includes geofence registration logic and a broadcast receiver for entry and exit transitions.
- **Nearby places** — Includes a Places API integration and Compose screen for nearby searches; the screen is not yet connected to the main navigation.

## Tech Stack

| Layer | Technologies |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Maps and location | Google Maps SDK, Maps Compose, Google Play services Location |
| Architecture | MVVM, ViewModel, LiveData, Kotlin Coroutines |
| Local storage | Room (SQLite) |
| Networking | Retrofit, Gson, Google Maps web service endpoints |
| Background work | WorkManager (geocoding worker scaffold) |
| Navigation | Navigation Compose |

## Project Structure

```text
app/src/main/
├── java/com/gabriel/gabrielcastellon_comp304lab4_ex1/
│   ├── MainActivity.kt              # App entry point and navigation
│   ├── GeoFence.kt                 # Geofence setup
│   ├── GeofenceBroadcastReceiver.kt
│   ├── GeocodeWorker.kt            # Background worker scaffold
│   ├── model/                      # Room entities, DAO, database, API models
│   ├── view/                       # Jetpack Compose screens
│   ├── viewmodel/                  # ViewModel, repository, factory
│   └── ui/theme/                   # App theme
└── AndroidManifest.xml
```

## Getting Started

**Requirements:** Android Studio, an Android SDK compatible with the project (compile SDK 35; minimum SDK 26), JDK 17, and a Google Cloud project configured for the APIs you plan to use.

1. Clone the repository and open the project directory in Android Studio.
2. Let Gradle sync and install any missing SDK components.
3. Create a `local.properties` file in the project root and add your own key:

   ```properties
   MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
   ```

4. Configure the app's Gradle build and manifest to read `MAPS_API_KEY` from `local.properties`. Keep that file excluded from Git. API requests made through Retrofit also need their credentials configured separately in the repository layer; **do not hardcode keys in Kotlin files**.
5. Enable the required Google Maps services in Google Cloud, configure appropriate key restrictions, and check the Android manifest permissions before running. Google Maps SDK credentials and web-service credentials may require different restrictions or a backend proxy.
6. Run the app on an emulator with Google Play services or an Android device. Grant location permissions when prompted.

## Development Notes

This is an educational Android project with features at different stages of completion. The map and local data flows are implemented in source; API-backed directions, geocoding, and places searches depend on external service configuration. The nearby-places screen is not yet part of the primary navigation, the WorkManager geocoding task is a scaffold, and geofence event delivery still requires manifest registration and device testing.

The project has not been verified as a production-ready build.

---

Developed by **Gabriel Castellon**.
