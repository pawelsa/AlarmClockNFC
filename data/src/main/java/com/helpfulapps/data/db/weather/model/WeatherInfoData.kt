package com.helpfulapps.data.db.weather.model

import com.helpfulapps.data.db.weather.converter.WeatherInfoConverter
import com.helpfulapps.domain.models.weather.WeatherInfo


data class WeatherInfoData(
    val temperature: TemperatureData = TemperatureData.NO_DATA,
    val rain: RainData = RainData.NO_DATA,
    val wind: WindData = WindData.NO_DATA,
    val snow: SnowData = SnowData.NO_DATA
) {

    fun toDomain(): WeatherInfo {
        return WeatherInfo(
            WeatherInfoConverter.getTemperature(temperature),
            WeatherInfoConverter.getRain(rain),
            WeatherInfoConverter.getWind(wind),
            WeatherInfoConverter.getSnow(snow)
        )
    }

}

enum class TemperatureData(val importance: Int) {
    VERY_COLD(-2),
    COLD(-1),
    NORMAL(0),
    HOT(1),
    VERY_HOT(2),
    NO_DATA(-10)
}

enum class WindData(val importance: Int) {
    NORMAL(0),
    WINDY(1),
    VERY_WINDY(2),
    NO_DATA(-10),
}

enum class RainData(val importance: Int) {
    NO_RAIN(0),
    MAY_RAIN(1),
    WILL_RAIN(2),
    HEAVY_RAIN(3),
    NO_DATA(-10)
}

enum class SnowData(val importance: Int) {
    NORMAL(0),
    SNOWY(1),
    VERY_SNOWY(2),
    NO_DATA(-10)
}