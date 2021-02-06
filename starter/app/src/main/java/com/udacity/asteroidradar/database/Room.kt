package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

@Dao
interface AsteroidDao {
    @Query("select * from $ASTEROID_TABLE order by closeApproachDate")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from $ASTEROID_TABLE where closeApproachDate = :date")
    fun getTodayAsteroids(date: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from $ASTEROID_TABLE where closeApproachDate between :startDate and :endDate order by closeApproachDate")
    fun getAsteroidsByDateInterval(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: DatabaseAsteroid)
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroid_radar")
                    .build()
        }
    }
    return INSTANCE
}
