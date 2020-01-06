package com.helpfulapps.alarmclock.views.ringing_alarm

data class WeatherData(
    var currentTemperature: Double? = null,
    var laterTemperature: Double? = null,
    var averageRain: Double? = null,
    var averageSnow: Double? = null,
    var averageWind: Double? = null
)