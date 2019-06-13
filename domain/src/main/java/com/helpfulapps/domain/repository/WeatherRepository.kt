package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.weather.Forecast
import com.helpfulapps.domain.models.weather.Weather
import io.reactivex.Completable
import io.reactivex.Single

interface WeatherRepository {

    fun downloadForecast(city: String): Completable

    fun downloadForecast(lat: Long, lon: Long): Completable

    fun getForecastForAlarms(): Single<Forecast>

    fun getForecastForAlarm(time: Long): Single<Weather>

}