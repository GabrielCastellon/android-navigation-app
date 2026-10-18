package com.gabriel.gabrielcastellon_comp304lab4_ex1.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    // Inserts a new location into the database. If a location with the same ID exists, it replaces it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location)

    // Retrieves all saved locations from the database.
    @Query("SELECT * FROM Locations")
    suspend fun getAllLocations(): List<Location>

    // Deletes all saved locations from the database.
    @Query("DELETE FROM Locations")
    suspend fun clearAll()
}
