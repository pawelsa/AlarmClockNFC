package com.helpfulapps.domain.models.weather

data class HourWeather(
    val id: Int = 0,
    val dt: Long = 0,
    val clouds: Int = 0,
    val rain: Double = 0.0,
    val snow: Double = 0.0,
    val wind: Double = 0.0,
    val humidity: Int = 0,
    val pressure: Double = 0.0,
    val temp: Double = 0.0,
    val tempMax: Double = 0.0,
    val tempMin: Double = 0.0,
    val weatherInfo: WeatherInfo = WeatherInfo()
)