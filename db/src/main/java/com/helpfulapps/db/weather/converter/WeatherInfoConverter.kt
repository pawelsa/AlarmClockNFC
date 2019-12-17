package com.helpfulapps.db.weather.converter

import com.helpfulapps.data.db.weather.model.RainData
import com.helpfulapps.data.db.weather.model.SnowData
import com.helpfulapps.data.db.weather.model.TemperatureData
import com.helpfulapps.data.db.weather.model.WindData


object WeatherInfoConverter {

    fun getTemperature(temperature: Int): TemperatureData {
        return when (temperature) {
            -2 -> TemperatureData.VERY_COLD
            -1 -> TemperatureData.COLD
            0 -> TemperatureData.NORMAL
            1 -> TemperatureData.HOT
            2 -> TemperatureData.VERY_HOT
            else -> TemperatureData.NO_DATA
        }
    }

    fun getRain(rain: Int): RainData {
        return when (rain) {
            0 -> RainData.NO_RAIN
            1 -> RainData.MAY_RAIN
            2 -> RainData.WILL_RAIN
            3 -> RainData.HEAVY_RAIN
            else -> RainData.NO_DATA
        }
    }

    fun getWind(wind: Int): WindData {
        return when (wind) {
            0 -> WindData.NORMAL
            1 -> WindData.WINDY
            2 -> WindData.VERY_WINDY
            else -> WindData.NO_DATA
        }
    }

    fun getSnow(snow: Int): SnowData {
        return when (snow) {
            0 -> SnowData.NORMAL
            1 -> SnowData.SNOWY
            2 -> SnowData.VERY_SNOWY
            else -> SnowData.NO_DATA
        }
    }

}