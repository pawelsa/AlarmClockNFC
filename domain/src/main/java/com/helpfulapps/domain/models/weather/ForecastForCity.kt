package com.helpfulapps.domain.models.weather

data class ForecastForCity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
)