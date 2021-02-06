package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.api.PictureOfDayApi
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        getPictureOfDay()
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