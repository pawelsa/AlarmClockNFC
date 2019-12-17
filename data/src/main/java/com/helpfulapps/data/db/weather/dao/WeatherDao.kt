package com.helpfulapps.data.db.weather.dao

import com.helpfulapps.data.db.weather.model.DayWeatherData
import io.reactivex.Flowable
import io.reactivex.Single

interface WeatherDao {
    fun insert(dayWeatherData: DayWeatherData): Single<Boolean>
    fun clearWeatherTables()
    fun streamWeatherList(): Flowable<DayWeatherData>
    fun getWeatherForTime(time: Long): Single<DayWeatherData>
}