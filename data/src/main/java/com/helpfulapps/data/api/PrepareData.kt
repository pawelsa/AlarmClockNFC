package com.helpfulapps.data.api

import com.helpfulapps.data.db.weather.model.DayWeatherData
import io.reactivex.Single

interface PrepareData {

    fun downloadForecast(
        cityName: String
    ): Single<List<DayWeatherData>>

    fun downloadForecastForCoordinates(
        lat: Double, lon: Double
    ): Single<List<DayWeatherData>>
}