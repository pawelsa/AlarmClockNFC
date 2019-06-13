package com.helpfulapps.data.api.weather.model

import com.helpfulapps.data.db.weather.model.ForecastDbModel

data class ForecastForCity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
) {

    fun toDbModel() = ForecastDbModel(
        cityName = city.name,
        timezone = city.timezone,
        weatherList = list.map { weather -> weather.toDbModel() }
    )

}