package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ASTEROID_TABLE = "asteroids"

@Entity(tableName = ASTEROID_TABLE)
data class DatabaseAsteroid constructor(
        @PrimaryKey val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
)
