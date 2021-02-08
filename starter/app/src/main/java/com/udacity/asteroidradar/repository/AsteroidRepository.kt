package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidDao.getAllAsteroids()) {
                it.asDomainModel()
            }

    val todayAsteroids: LiveData<List<Asteroid>>
        get() {
            val today = Date()
            val formattedToday = formatDate(today)
            return Transformations.map(database.asteroidDao.getTodayAsteroids(formattedToday)) {
                it.asDomainModel()
            }
        }

    val weekAsteroids: LiveData<List<Asteroid>>
        get() {
            val startDate = Date()
            val endDate = addDays(startDate)
            val formattedStartDate = formatDate(startDate)
            val formattedEndDate = formatDate(endDate)
            return Transformations.map(database.asteroidDao.getAsteroidsByDateInterval(
                    startDate = formattedStartDate,
                    endDate = formattedEndDate
            )) {
                it.asDomainModel()
            }
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val networkAsteroids = AsteroidApi.service.getAsteroids()
                val parsedResult = parseAsteroidsJsonResult(jsonResult = JSONObject(networkAsteroids))

                database.asteroidDao.insertAllAsteroids(*parsedResult.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}