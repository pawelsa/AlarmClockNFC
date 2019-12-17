package com.helpfulapps.data.db.weather.model

import com.helpfulapps.domain.models.weather.DayWeather

data class DayWeatherData(
    var id: Int = 0,
    var dt: Long = 0,
    var cityName: String = "",
    var hourWeatherList: List<HourWeatherData> = listOf(),
    var weatherInfo: WeatherInfoData = WeatherInfoData()
) {

    fun toDomain(): DayWeather {
        return DayWeather(
            id, dt, cityName, hourWeatherList.map { it.toDomain() }, weatherInfo.toDomain()
        )
    }

}