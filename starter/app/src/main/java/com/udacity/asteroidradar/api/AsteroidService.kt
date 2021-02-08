package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidApiFilter(val value: String) {
    SHOW_TODAY("today"),
    SHOW_WEEK("week"),
    SHOW_ALL("all")
}

interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
            @Query("api_key") apiKey: String = Constants.API_KEY
    ): String
}

object AsteroidApi {
    val service: AsteroidService by lazy { retrofit.create(AsteroidService::class.java) }
}

private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(getHttpClient())
        .build()
