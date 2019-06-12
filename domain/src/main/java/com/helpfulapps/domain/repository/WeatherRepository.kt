package com.helpfulapps.domain.repository

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.models.weather.Forecast
import io.reactivex.Completable
import io.reactivex.Single

interface WeatherRepository {

    fun downloadForecast(city: String, time: Long): Completable

    fun downloadForecast(lat: Long, lon: Long, time: Long): Completable

    fun getForecastForAlarms() : Single<List<Forecast>>

    fun getForecastForAlarm(alarm : Alarm) : Single<Forecast>

}