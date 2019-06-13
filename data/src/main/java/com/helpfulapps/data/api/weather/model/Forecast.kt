package com.helpfulapps.data.api.weather.model

import com.helpfulapps.data.db.weather.model.WeatherDbModel

data class Forecast(
    val clouds: Clouds?,
    val rain: Rain?,
    val snow: Snow?,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind?
) {
    fun toDbModel() =
        WeatherDbModel(
            timestamp = dt,
            cloudPercent = clouds?.all ?: 0,
            windSpeed = wind?.speed ?: 0.0,
            snowIn3h = snow?._3h ?: 0.0,
            rainIn3h = rain?._3h ?: 0.0,
            temp = main.temp,
            tempMax = main.temp_max,
            tempMin = main.temp_min,
            description = weather.first().description,
            icon = weather.first().icon
        )

}