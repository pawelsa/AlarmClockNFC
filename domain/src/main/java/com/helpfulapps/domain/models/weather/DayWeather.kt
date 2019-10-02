package com.helpfulapps.domain.models.weather


data class DayWeather(
    val id: Int = 0,
    val dt: Long = 0,
    val cityName: String = "",
    val hourWeatherList: List<HourWeather> = listOf(),
    val weatherInfo: WeatherInfo = WeatherInfo()
)