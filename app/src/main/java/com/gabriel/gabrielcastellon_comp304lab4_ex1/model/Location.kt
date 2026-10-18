package com.gabriel.gabrielcastellon_comp304lab4_ex1.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Locations")
data class Location(
    // Unique ID for each location (auto-generated).
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Latitude coordinate of the location
    @ColumnInfo(name = "latitude") val latitude: Double,
    // Longitude coordinate of the location
    @ColumnInfo(name = "longitude") val longitude: Double,
    // Timestamp when the location was saved.
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    // User-defined or API-provided name for the location.
    @ColumnInfo(name = "name") val name: String
)
