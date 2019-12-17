package com.helpfulapps.data.db.weather.dao

import com.helpfulapps.data.db.weather.model.DayWeather
import io.reactivex.Flowable
import io.reactivex.Single

interface WeatherDao {
    fun streamWeatherList(): Flowable<DayWeather>
    fun getWeatherForTime(): Single<DayWeather>
    fun <T> deleteTable(tableClass: Class<T>)
}