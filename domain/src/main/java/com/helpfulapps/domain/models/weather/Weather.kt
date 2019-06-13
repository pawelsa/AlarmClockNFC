package com.helpfulapps.domain.models.weather

data class Weather(
    val timestamp: Int,
    val cloudPercent: Int,
    val windSpeed: Double,
    val snowIn3h: Double,
    val rainIn3h: Double,
    val temp: Double,
    val tempMax: Double,
    val tempMin: Double,
    val description: String,
    val icon: String
)