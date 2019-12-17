package com.helpfulapps.data.api.weather.model

import com.helpfulapps.data.db.weather.model.DayWeatherEntity

data class ForecastForCity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
) {

    fun toDbModel() =
        DayWeatherEntity(
            dt = list.first().dt,
            cityName = city.name,
            hourWeatherEntityList = list.map { it.toDbModel() }
        )
}