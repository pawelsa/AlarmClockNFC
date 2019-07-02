package com.helpfulapps.domain.models.alarm

import com.helpfulapps.domain.models.weather.DayWeather

// dayWeather, hourWeather
data class WeatherAlarm(
    val alarm: Alarm,
    val dayWeather: DayWeather
)