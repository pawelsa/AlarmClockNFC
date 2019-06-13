package com.helpfulapps.domain.models.weather

data class Forecast(
    var cityName: String,
    var timezone: Int,
    var weatherList: List<Weather>
)