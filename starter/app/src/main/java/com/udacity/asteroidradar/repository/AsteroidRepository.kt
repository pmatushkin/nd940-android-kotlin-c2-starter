package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

class AsteroidRepository(private val database: AsteroidDatabase) {

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