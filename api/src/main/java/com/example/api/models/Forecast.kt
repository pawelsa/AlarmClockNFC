package com.example.api.models

import com.helpfulapps.data.db.weather.model.HourWeatherData


data class Forecast(
    val clouds: Clouds?,
    val rain: Rain?,
    val snow: Snow?,
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind?
) {
    fun toData() =
        HourWeatherData(
            dt = dt,
            clouds = clouds?.all ?: 0,
            rain = rain?._3h ?: 0.0,
            snow = snow?._3h ?: 0.0,
            wind = wind?.speed ?: 0.0,
            humidity = main.humidity,
            pressure = main.pressure,
            temp = main.temp,
            tempMax = main.temp_max,
            tempMin = main.temp_min
        )
}