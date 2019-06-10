package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.weather.Forecast
import io.reactivex.Single

interface WeatherRepository {

    fun getForecast(city: String, time: Long): Single<Forecast>

    fun getForecast(lat: Long, lon: Long, time: Long): Single<Forecast>

}