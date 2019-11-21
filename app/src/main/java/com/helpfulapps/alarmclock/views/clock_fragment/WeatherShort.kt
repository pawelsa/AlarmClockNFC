package com.helpfulapps.alarmclock.views.clock_fragment

import com.helpfulapps.domain.models.weather.WeatherInfo

data class WeatherShort(
    val temperature: Int = 0,
    val rain: Int = 0,
    val wind: Int = 0,
    val snow: Int = 0
) {
    constructor(weatherInfo: WeatherInfo) : this(
        temperature = if (weatherInfo.temperature.importance == -10) 0 else weatherInfo.temperature.importance,
        rain = if (weatherInfo.rain.importance == -10) 0 else weatherInfo.rain.importance,
        wind = if (weatherInfo.wind.importance == -10) 0 else weatherInfo.wind.importance,
        snow = if (weatherInfo.snow.importance == -10) 0 else weatherInfo.snow.importance
    )
}