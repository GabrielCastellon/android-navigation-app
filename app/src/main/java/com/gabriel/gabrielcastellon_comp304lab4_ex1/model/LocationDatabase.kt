package com.gabriel.gabrielcastellon_comp304lab4_ex1.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Location::class], version = 1)
abstract class LocationDatabase: RoomDatabase() {
    // The LocationDAO object
    abstract fun getLocationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                )
                    .fallbackToDestructiveMigration() // Handles migration by destroying old data
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}