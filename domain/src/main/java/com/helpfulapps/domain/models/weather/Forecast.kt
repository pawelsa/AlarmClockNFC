package com.helpfulapps.domain.models.weather

data class Forecast(
    val time : Long,
    val maxTemp : Double,
    val minTemp : Double,
    val temp : Double,
    val rainAtAlarmTime : Boolean,
    val rainDuringDay : Boolean,
    val rainProbability : Double
)