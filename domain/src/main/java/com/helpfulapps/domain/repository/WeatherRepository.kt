package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.weather.DayWeather
import io.reactivex.Completable
import io.reactivex.Single

interface WeatherRepository {

    fun downloadForecast(city: String): Completable

    fun downloadForecast(lat: Long, lon: Long): Completable

    fun getForecastForAlarms(): Single<List<DayWeather>>

    fun getForecastForAlarm(time: Long): Single<DayWeather>

}