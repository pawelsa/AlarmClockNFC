package com.helpfulapps.domain.models.alarm

import com.helpfulapps.domain.models.weather.WeatherInfo

data class WeatherAlarm(
    val alarm: Alarm,
    val weatherInfo: WeatherInfo
)