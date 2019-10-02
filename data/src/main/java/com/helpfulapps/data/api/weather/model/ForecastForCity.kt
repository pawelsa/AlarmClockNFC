package com.helpfulapps.data.api.weather.model

import com.helpfulapps.data.db.weather.model.DayWeather

data class ForecastForCity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
) {

    fun toDbModel() =
        DayWeather(
            dt = list.first().dt,
            cityName = city.name,
            hourWeatherList = list.map { it.toDbModel() }
        )
}