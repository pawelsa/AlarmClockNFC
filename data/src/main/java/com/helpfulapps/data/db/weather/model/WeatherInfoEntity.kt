package com.helpfulapps.data.db.weather.model

import com.helpfulapps.domain.models.weather.*


data class WeatherInfoData(
    val temperature: Temperature = Temperature.NO_DATA,
    val rain: Rain = Rain.NO_DATA,
    val wind: Wind = Wind.NO_DATA,
    val snow: Snow = Snow.NO_DATA
) {

    fun toDomain(): WeatherInfo {
        return WeatherInfo(
            temperature, rain, wind, snow
        )
    }

}