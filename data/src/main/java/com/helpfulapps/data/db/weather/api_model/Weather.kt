package com.helpfulapps.data.db.weather.api_model

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)