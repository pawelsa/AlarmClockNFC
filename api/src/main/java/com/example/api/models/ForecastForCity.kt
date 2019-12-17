package com.example.api.models

import com.helpfulapps.data.db.weather.model.DayWeatherEntity

data class ForecastForCity(
    val city: com.example.api.models.City,
    val cnt: Int,
    val cod: String,
    val list: List<com.example.api.models.Forecast>,
    val message: Double
) {

    fun toDbModel() =
        DayWeatherEntity(
            dt = list.first().dt,
            cityName = city.name,
            hourWeatherEntityList = list.map { it.toDbModel() }
        )
}