package com.helpfulapps.data.db.weather.dao

import com.helpfulapps.data.db.weather.model.DayWeatherEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface WeatherDao {
    fun streamWeatherList(): Flowable<DayWeatherEntity>
    fun getWeatherForTime(): Single<DayWeatherEntity>
    fun <T> deleteTable(tableClass: Class<T>)
}