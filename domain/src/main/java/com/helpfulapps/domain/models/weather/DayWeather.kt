package com.helpfulapps.domain.models.weather


data class DayWeather(
    var id: Int = 0,
    var dt: Long = 0,
    var cityName: String = "",
    var hourWeatherList: List<HourWeather> = listOf()
)