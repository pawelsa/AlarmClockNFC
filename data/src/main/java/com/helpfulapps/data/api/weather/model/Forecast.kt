package com.helpfulapps.data.api.weather.model

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
)