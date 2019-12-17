package com.helpfulapps.data.db.weather.model

import com.helpfulapps.domain.models.weather.HourWeather

data class HourWeatherData(
    var id: Int = 0,
    var dt: Long = 0,
    var clouds: Int = 0,
    var rain: Double = 0.0,
    var snow: Double = 0.0,
    var wind: Double = 0.0,
    var humidity: Int = 0,
    var pressure: Double = 0.0,
    var temp: Double = 0.0,
    var tempMax: Double = 0.0,
    var tempMin: Double = 0.0,
    var weatherInfo: WeatherInfoData? = null
) {
    fun toDomain(): HourWeather {
        return HourWeather(
            id,
            dt,
            clouds,
            rain,
            snow,
            wind,
            humidity,
            pressure,
            temp,
            tempMax,
            tempMin,
            weatherInfo.toDomain()
        )
    }
}