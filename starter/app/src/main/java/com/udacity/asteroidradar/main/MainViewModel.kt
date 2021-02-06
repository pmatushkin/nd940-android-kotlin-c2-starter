package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.PictureOfDayApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application.applicationContext)
    private val repository = AsteroidRepository(database)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        getPictureOfDay()

        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                val apod = PictureOfDayApi.service.getPictureOfDay()

                if (apod.mediaType == "image") {
                    _pictureOfDay.value = apod
                }
            } catch (e: Exception) {
                _pictureOfDay.value = null
            }
        }
    }
}