package com.helpfulapps.data.db.weather.model

data class ForecastForCity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
)