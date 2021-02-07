package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApiFilter
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

    private val _asteroidFilter = MutableLiveData<AsteroidApiFilter>()

    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid>
        get() = _navigateToDetail

    init {
        getPictureOfDay()

        viewModelScope.launch {
            repository.refreshAsteroids()
        }

        updateAsteroidList(AsteroidApiFilter.SHOW_ALL)
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

    val asteroids = Transformations.switchMap(_asteroidFilter) {
        when(it) {
            AsteroidApiFilter.SHOW_WEEK -> {
                repository.weekAsteroids
            }
            AsteroidApiFilter.SHOW_TODAY -> {
                repository.todayAsteroids
            }
            else -> {
                repository.asteroids
            }
        }
    }

    fun updateAsteroidList(filter: AsteroidApiFilter) {
        _asteroidFilter.value = filter
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToDetail.value = null
    }
}